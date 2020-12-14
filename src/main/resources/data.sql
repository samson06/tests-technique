-- Insertion dans la table BAND 
INSERT INTO BAND(ID, NAME) 
VALUES 
(1000, 'Pink Floyd'),
(1001, 'Guns n roses'),
(1002, 'Metallica'),
(1003, 'Rolling Stones'),
(1004, 'The Ramones'),
(1005, 'Megadeth'),
(1006, 'AC/DC'),
(1007, 'Deep Purple'),
(1008, 'Sum41'),
(1009, 'Off Spring');

-- Insertion dans la table MEMBER 
INSERT INTO MEMBER(ID, NAME) 
VALUES 
(1001,'Queen Frankie Gross (Fania)'),
(1002,'Queen Genevieve Clark'),
(1003,'Queen Veronica Graves'),
(1004,'Queen Stacey ODoherty (Asya)'),
(1005,'Queen Gertrude Hudson'),
(1006,'Queen Madeleine Taylor'),
(1007,'Queen Jasmine Collier'),
(1008,'Queen Daisy Burke'),
(1009,'Queen Aaliyah York'),
(1010,'Queen Anika Walsh'),
(1011,'Queen Katy Stone'),
(1012,'Queen Aliyah Jarvis'),
(1013,'Queen Constance Carroll'),
(1014,'Queen Talia Bush'),
(1015,'Queen Ava Dunlap'),
(1016,'Queen Haleema Poole'),
(1017,'Queen Robbie Bender'),
(1018,'Queen Laila Shelton'),
(1019,'Queen Eleanor Fisher (Ellie)'),
(1020,'Queen Abigail Cardenas'),
(1021,'Queen Kimberly Jacobs'),
(1022,'Queen Crystal Lynn'),
(1023,'Queen Felix Nichols'),
(1024,'Queen Victoria Cooper'),
(1025,'Queen Charlie Wolf (Chick)'),
(1026,'Queen Jamie Petty'),
(1027,'Queen Danielle Connor (Dannon)'),
(1028,'Queen Betty Thomas (Ilsa)'),
(1029,'Queen Annabel Hardy'),
(1030,'Queen Yasmine Buckley');

-- Insertion dans la table BAND_MEMBERS 
INSERT INTO BAND_MEMBERS(BAND_ID, MEMBERS_ID) 
VALUES 
(1000,1001),
(1000,1002),
(1000,1003),
(1000,1004),
(1000,1005),
(1000,1006),
(1001,1007),
(1001,1008),
(1001,1009),
(1002,1010),
(1002,1011),
(1002,1012),
(1002,1013),
(1003,1014),
(1004,1015),
(1004,1016),
(1005,1017),
(1005,1018),
(1005,1019),
(1006,1020),
(1006,1021),
(1006,1022),
(1006,1023),
(1007,1024),
(1008,1025),
(1008,1026),
(1008,1027),
(1008,1028),
(1009,1029),
(1009,1030);

-- Insertion dans la table EVENT 
INSERT INTO EVENT(ID, TITLE, IMG_URL) 
VALUES 
(1000, 'GrasPop Metal Meeting', 'img/1000.jpeg'),
(1001, 'Alcatraz Fest', 'img/1001.jpeg'),
(1002, 'Les Vieilles Charrues', 'img/1002.jpeg'),
(1003, 'Download Festival', 'img/1003.jpeg'),
(1004, 'Motocultor', 'img/1004.jpeg');

-- Insertion dans la table EVENT_BANDS 
INSERT INTO EVENT_BANDS(EVENT_ID, BANDS_ID) 
VALUES 
(1000, 1000),
(1000, 1001),
(1000, 1002),
(1000, 1003),
(1000, 1004),
(1001, 1005),
(1002, 1006),
(1003, 1000),
(1004, 1007),
(1004, 1008),
(1004, 1009);