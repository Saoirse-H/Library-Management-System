package ie.ucd.RapidEyeMovement.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import ie.ucd.RapidEyeMovement.model.*;

@Controller
public class RapidEyeMovementController {

    @Autowired
    private UserSession userSession;
    @Autowired
    private ArtifactRepository artifactRepository;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private ReserveRepository reserveRepository;

    long loanTime = 14 * 24 * 3600 * 1000;
    float lateFee = 1.00f;
    boolean alreadyReserved = false;

    @GetMapping("/")
    public String index(Model model) {
        if(userSession.isWrongRole())
        {
            model.addAttribute("error", "You cannot access this page");
            userSession.setWrongRole(false);
        }
        model.addAttribute("artifacts", artifactRepository.findAll());
        model.addAttribute("user", userSession.getUser());
        return "index.html";
    }    

    @PostMapping("/search")
    public String search(Model model, String keyword) throws IOException {
        List<Artifact> artifacts = artifactRepository.keywordSearch(keyword);
        model.addAttribute("artifacts", artifacts);
        model.addAttribute("user", userSession.getUser());
        return "index.html";
    }

    @PostMapping("/search-adv")
    public String advancedSearch(Model model, String title, String author, String media, String stock) throws Exception {
        if(stock == null)
            stock = "Loan";

        List<Artifact> artifacts = artifactRepository.advancedSearch(title, author, media, stock);

        model.addAttribute("artifacts", artifacts);
        model.addAttribute("user", userSession.getUser());

        return "index.html";
    }

    @PostMapping("/search-member")
    public String searchMember(Model model, String keyword) throws IOException {
        List<User> members = userRepository.memberSearch(keyword);
        List<Artifact> artifacts = artifactRepository.findAll();
        model.addAttribute("artifacts", artifacts);
        model.addAttribute("members", members);
        model.addAttribute("user", userSession.getUser());
        return "admin.html";
    }

    @PostMapping("/search-stock")
    public String searchStock(Model model, String keyword) throws IOException {
        List<Artifact> artifacts = artifactRepository.keywordSearch(keyword);
        List<User> members = userRepository.findAll();
        model.addAttribute("artifacts", artifacts);
        model.addAttribute("member", members);
        model.addAttribute("user", userSession.getUser());
        return "admin.html";
    }

    @GetMapping("/view")
    public String view(@RequestParam("id") long artifactId, Model model) {
        User user = userSession.getUser();
        if(user != null) {
            model.addAttribute("memberId", user.getId());
        } else {
            model.addAttribute("memberId", -1);
        }

        model.addAttribute("error", null);
        Artifact artifact = artifactRepository.getOne(artifactId);
        model.addAttribute("user", userSession.getUser());
        model.addAttribute("artifact", artifact);
        model.addAttribute("inStock", artifactRepository.inStock(artifactId));
        return "view.html";
    }

    @GetMapping("/request")
    public String request(@RequestParam("id") long artifact_id, Model model) {
        User user = userSession.getUser();
        if (user == null){
            model.addAttribute("error", "You must be logged in to request artifacts");
        }

        //  No available book to request - Throw error
        int onShelf = artifactRepository.inStock(artifact_id);
        int reservedForUser = reserveRepository.hasReservation(artifact_id, user.getId());

        if(onShelf == 0 && reservedForUser == 0) {
            Artifact artifact = artifactRepository.getOne(artifact_id);
            model.addAttribute("error", "This " + artifact.getMedia().toLowerCase() + " is not available at the moment.");
            model.addAttribute("user", user);
            model.addAttribute("artifact", artifact);
            model.addAttribute("inStock", artifactRepository.inStock(artifact_id));
            return "view.html";
        }

        //  Book Reserved for user - Mark on Shelf and Remove from Reserved List
        if(reservedForUser != 0) {
            Stock book = stockRepository.getReserved(artifact_id).get(0); 
            book.setLocation("Shelf");
            stockRepository.save(book);

            Reserve reservation = reserveRepository.getTopOfList(artifact_id).get(0);
            Long next_id = reservation.getAfter();
            reserveRepository.delete(reservation);

            if(next_id != null) {
                reservation = reserveRepository.getOne(next_id);
                reservation.setBefore(null);
                reserveRepository.save(reservation);
            }
        }
        
        //  Loan book to user
        Stock book = stockRepository.getAvailable(artifact_id).get(0);

        Date currentDate = new Date();
        Date returnDate = new Date(System.currentTimeMillis() + loanTime);

        stockRepository.loanBook(book.getId(), currentDate, returnDate, user);

        //  Update Loan History
        Artifact artifact = artifactRepository.findById(artifact_id).get();

        History history = new History();
        history.setArtifact(artifact);
        history.setReturned(false);     
        history.setUser(user);
        historyRepository.save(history);

        return member(user.getId(), model);
    }

    @RequestMapping(value={"/renew/{memberId}/{bookId}"}, method = RequestMethod.GET)
    public String renew(@PathVariable("memberId") long memberId, @PathVariable("bookId") long bookId, Model model)
    {
        List<Reserve> reserved = reserveRepository.alreadyReserved(bookId);
        if(reserved == null)
        {
            Stock book = stockRepository.getOnLoan(bookId, memberId).get(0);
            Date returnDate = new Date(System.currentTimeMillis() + loanTime);
            book.setDue(returnDate);
            stockRepository.save(book);
        }
        else
            alreadyReserved = true;

        return member(memberId, model);

    }

    @RequestMapping(value={"/return/{memberId}/{bookId}"}, method = RequestMethod.GET)
    public String returnBook(@PathVariable("memberId") Long memberId, @PathVariable("bookId") long bookId, Model model) throws IOException {
        User user = userRepository.getOne(memberId);
        Stock book = stockRepository.getOnLoan(bookId, user.getId()).get(0);

        Date dueDate = book.getDue();

        if(dueDate.before(new Date())) {
            float fee = user.getLateFee() + lateFee;
            user.setLateFee(fee);
            userRepository.save(user);
        }
        
        book.setChecked(null);
        book.setDue(null);
        book.setUser(null);

        List<Reserve> r = reserveRepository.getTopOfList(book.getArtifact().getId());
        if(r.size() > 0) {
            book.setLocation("Reserved");
        }
        else {
            book.setLocation("Shelf");
        }
        
        stockRepository.save(book);
        History history = historyRepository.findNotReturned(bookId, user.getId()).get(0);
        historyRepository.returnBook(history.getId());
        model.addAttribute("user", userSession.getUser());
        return member(user.getId(), model);
    }

    @RequestMapping(value={"/reserve/{memberId}/{bookId}"}, method = RequestMethod.GET)
    public void reserveBook(@PathVariable("memberId") long memberId, @PathVariable("bookId") long bookId, Model model, HttpServletResponse response) throws IOException {
        User user = userRepository.getOne(memberId);
        int inStock = artifactRepository.inStock(bookId);

        Reserve reservation = new Reserve();
        reservation.setArtifact(artifactRepository.getOne(bookId));
        reservation.setUser(user);

        List<Reserve> r = reserveRepository.getLastInQueue(bookId);
        if (r.size() > 0) {
            Reserve ahead = r.get(0);
            reservation.setBefore(ahead.getId());
            reserveRepository.save(reservation);

            ahead.setAfter(reservation.getId());
            reserveRepository.save(ahead);
        } else {
            reserveRepository.save(reservation);
        }

        // Mark Book as reserved
        if (inStock > 0) {
            Stock book = stockRepository.getAvailable(bookId).get(0);
            book.setLocation("Reserved");
            stockRepository.save(book);
        }

        response.sendRedirect("/member?id="+memberId);
    }

    @GetMapping("/member")
    public String member(@RequestParam("id") long id, Model model) {
        User user = userSession.getUser();
        User member = userRepository.getOne(id);
        
        List<Artifact> readyToCollect = artifactRepository.getReservedCollection(member.getId());
        List<UserBooks> books = artifactRepository.getUserBooks(member.getId());
        List<UserBooks> overdue = artifactRepository.getOverdue(member.getId());
        List<UserBooks> history = artifactRepository.getHistory(member.getId());
        List<UserBooks> reserved = artifactRepository.getReserved(member.getId());

        if(alreadyReserved)
        {
            model.addAttribute("error", "You cannot renew this as it has been reserved for another user");
            alreadyReserved = false;
        }
        model.addAttribute("size", readyToCollect.size());
        if(readyToCollect.size() > 0)
            model.addAttribute("collect", readyToCollect.get(0).getTitle());
        model.addAttribute("user", user);
        model.addAttribute("member", member);
        model.addAttribute("loan", books);
        model.addAttribute("overdue", overdue);
        model.addAttribute("history", history);
        model.addAttribute("reserved", reserved);
        return "member.html";
    }

    @GetMapping("/update")
    public String update(@RequestParam("id") long id, Model model) {
        model.addAttribute("member", userRepository.getOne(id));
        model.addAttribute("user", userSession.getUser());
        return "update.html";
    }

    @PostMapping("/update")
    public void update(@RequestParam("id") long id, String memberName, String memberEmail, String memberPassword, String memberAddress, String memberPhone, String memberBirthday, HttpServletResponse response) throws Exception {
        Optional<User> user = userRepository.findById(id);
        User updated = user.get();
        updated.setName(memberName);
        updated.setEmail(memberEmail);
        updated.setPassword(memberPassword);
        updated.setAddress(memberAddress);
        updated.setPhone(memberPhone);
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");
        Date birthday= format.parse(memberBirthday);
        updated.setBirthday(birthday);
        userRepository.save(updated);
        response.sendRedirect("/member?id="+updated.getId());
    }

    @GetMapping("/admin")
    public String admin(Model model, HttpServletResponse response) throws IOException
    {
        if(!userSession.getUser().getRole().equals("admin"))
        {
            userSession.setWrongRole(true);
            response.sendRedirect("/");
        }

        List<User> members = userRepository.findAll();
        List<Artifact> stock = artifactRepository.findAll();
        model.addAttribute("artifacts", stock);
        model.addAttribute("members", members);
        model.addAttribute("user", userSession.getUser());
        return "admin.html";
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("user", userSession.getUser());
        return "create.html";
    }

    @PostMapping("/create")
    public void create(String title, String synopsis, String stock, String author, String media, HttpServletResponse response) throws IOException {
        Artifact newArtifact = new Artifact();
        newArtifact.setTitle(title);
        newArtifact.setSynopsis(synopsis);
        newArtifact.setAuthor(author);
        newArtifact.setMedia(media);

        artifactRepository.save(newArtifact);

        int stockCount = Integer.parseInt(stock);
        for (int i=0; i < stockCount; i++) {
            Stock s = new Stock();
            s.setLocation("Shelf");
            s.setChecked(null);
            s.setDue(null);
            s.setArtifact(newArtifact);
            stockRepository.save(s);
        }

        response.sendRedirect("/admin");
    }

    @GetMapping("/remove")
    public RedirectView remove(@RequestParam("id") long id) {
        artifactRepository.deleteById(id);
        return new RedirectView("admin");
    }

    @GetMapping("/manage")
    public String manage(@RequestParam("id") long id, Model model) {
        List<Stock> stocks = stockRepository.getAllStockWithArtifactId(id);
        Artifact artifact = artifactRepository.getOne(id);
        List<User> members = userRepository.getMembers();
        model.addAttribute("user", userSession.getUser());
        model.addAttribute("stocks", stocks);
        model.addAttribute("members", members);
        model.addAttribute("artifact", artifact);
        return "manage.html";
    }

    @GetMapping("/toggle")
    public void toggle(@RequestParam("id")long id, HttpServletResponse response) throws IOException {
        Stock stock = stockRepository.getOne(id);
        String location = stock.getLocation();

        if (location.equals("Shelf")) {
            stock.setLocation("Loan");
            stockRepository.save(stock);
        }
        else {
            stock.setLocation("Shelf");
            History history = historyRepository.findNotReturned(stock.getArtifact().getId(), stock.getUser().getId()).get(0);
            historyRepository.returnBook(history.getId());
            stock.setUser(null);
            stock.setChecked(null);
            stock.setDue(null);
            stockRepository.save(stock);
        }

        response.sendRedirect("/manage?id="+stock.getArtifact().getId());
    }

    @PostMapping("/create-loan")
    public void createLoan(long stockId, long userId, HttpServletResponse response) throws IOException {

        Stock stock = stockRepository.getOne(stockId);
        User user = userRepository.getOne(userId);
        Date due = new Date(System.currentTimeMillis() + loanTime);

        stock.setUser(user);
        stock.setDue(due);
        stock.setChecked(new Date(System.currentTimeMillis()));
        stock.setLocation("Loan");
        stockRepository.save(stock);
        //  Update Loan History
        Artifact loaned = artifactRepository.findById(stock.getArtifact().getId()).get();
        History history = new History();
        history.setArtifact(loaned);
        history.setReturned(false);     
        history.setUser(user);
        historyRepository.save(history);
        
        response.sendRedirect("/manage?id="+stock.getArtifact().getId());
    }
}