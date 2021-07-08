INSERT INTO Artifact(id, title, synopsis, author, media) VALUES
    (0, '1984', 'A man called Winston has an affair with a younger office worker while his Big Brother watches.', 'George Orwell', 'Book'),
    (1, 'Frankenstein', 'An angry swamp zombie struggles with daddy issues and existentialism.', 'Mary Shelley', 'Book'),
    (2, 'Animal Farm', 'A bunch of animals throw a revolution.', 'George Orwell', 'Book'),
    (3, 'Wuthering Heights', 'A man gets very angry when his adoptive sister will not love him back.', 'Emily Bronte', 'Book'),
    (4, 'Singing In the Rain', 'When actors now have to talk in movies, those with annoying voices have to be dubbed.', 'Stanley Donen, Gene Kelly', 'DVD'),
    (5, 'Blonde on Blonde', 'Seventh album by Bob Dylan, there is a lot of stream of consciousness going on here.', 'Bob Dylan', 'CD'),
    (6, 'Harry Potter and the Philosophors Stone', 'Did you know in America, it is called the Sorcers Stone?', 'J.K. Rowling, Chris Columbus', 'DVD'),
    (7, 'sweetener', 'Fourth album by Ariana Grande, many people say this was one of the greatest albums of the decade. They are right.', 'Ariana Grande', 'CD'),
    (8, 'The Umbrella Academy: Apocalypse Suite', 'Comic book series written by Gerard Way (yes, that Gerard Way), which explores how childhood abuse effects people through the medium of superheroes.', 'Gerard Way', 'Comic'),
    (9, 'Watchmen', 'Superheroes in the 1980s, but only one guy is an actual superhero. Hijinks ensue.', 'Alan Moore', 'Comic');



INSERT INTO User(id, name, address, email, phone, role, password, joined, birthday, fee) VALUES
    (0, 'Max Bookington', '23 Generic Avenue', 'maximus@email.com', '0856666656', 'admin', 'password', '2002-06-12', '1966-11-02', 0.0),
    (1, 'Bob Reeds', '01 Everyday St', 'reader@email.com', '0867798332', 'member', 'password', '2004-03-25', '1968-02-21', 0.0),
    (2, 'Harriet Page', '53 Winding Way', 'page@email.com', '0836172833', 'member', 'cat', '2018-04-12', '2000-01-06', 0.0),
    (3, 'Joe Bloggs', '14 Sample Green', 'joebloggs@example.com', '0812345678', 'member', 'blogs', '2012-12-21', '1999-12-31', 0.0),
    (4, 'Ella Cover', '81 Rose Estate', 'coverella@email.com', '0891738472', 'member', 'dog', '2013-01-14', '2003-10-03', 0.0);

INSERT INTO Stock(id, location, artifact_id, user_id, checked, due) VALUES
    (0, 'Shelf', 0, null, null, null),
    (1, 'Loan', 0, 1, '2020-02-01', '2020-02-15'),
    (2, 'Shelf', 1, null, null, null),
    (3, 'Loan', 1, 1, '2020-02-01', '2020-02-15'),
    (4, 'Loan', 2, 3, '2020-01-05', '2020-01-19'),
    (5, 'Shelf', 3, null, null, null),
    (6, 'Loan', 3, 2, '2020-03-17', '2020-03-31'),
    (7, 'Shelf', 4, null, null, null),
    (8, 'Loan', 4, 4, '2020-03-11', '2020-03-25'),
    (9, 'Shelf', 5, null, null, null),
    (10, 'Loan', 6, 2, '2020-02-24', '2020-03-09'),
    (11, 'Loan', 6, 4, '2020-03-18', '2020-04-01'),
    (12, 'Loan', 7, 3, '2020-03-20', '2020-03-03'),
    (13, 'Shelf', 8, null, null, null),
    (14, 'Loan', 8, 1, '2020-02-01', '2020-02-15'),
    (15, 'Shelf', 9, null, null, null),  
    (16, 'Shelf', 9, null, null, null);

INSERT INTO History(id, returned, artifact_id, user_id) VALUES
    (0, 'FALSE', 0, 1),
    (1, 'FALSE', 1, 1),
    (2, 'FALSE', 2, 3),
    (3, 'FALSE', 3, 2),
    (4, 'FALSE', 4, 4),
    (5, 'FALSE', 6, 2),
    (6, 'FALSE', 6, 4),
    (7, 'FALSE', 7, 3),
    (8, 'FALSE', 8, 1),
    (9, 'TRUE', 3, 1),
    (10, 'TRUE', 8, 2),
    (11, 'TRUE', 5, 3),
    (12, 'TRUE', 7, 4);

INSERT INTO Reserve(id, artifact_id, user_id, before, after) VALUES
    (0, 2, 1, null, null),
    (1, 6, 3, null, null),
    (2, 7,4, null, null);