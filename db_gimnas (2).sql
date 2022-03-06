
CREATE DATABASE db_gimnas;
USE db_gimnas;

drop table if exists activitat;
create table activitat (
id integer auto_increment,
nom VARCHAR (255) not null,
descripcio VARCHAR (255),
color VARCHAR (50) not null,
durada_sessio integer not null, /***********minuts**************/
primary key (id)
);

drop table if exists activitat_lliure;
create table activitat_lliure (
id integer,
primary key (id),
FOREIGN KEY (id) REFERENCES activitat(id)
);

drop table if exists activitat_colectiva;
create table activitat_colectiva (
id integer,
primary key (id),
FOREIGN KEY (id) REFERENCES activitat(id)
);

drop table if exists sala;
create table sala (
num integer,
descripcio varchar(255),
aforament_max int not null,
primary key (num)
);

drop table if exists es_fa;
create table es_fa (
`data` date,
hora time,
num integer,
id integer,
primary key (id),
FOREIGN KEY (id) REFERENCES activitat(id),
FOREIGN KEY (num) REFERENCES sala(num)
);

drop table if exists monitor;
create table monitor (
dni varchar(9),
nom varchar(30) not null,
cognom varchar(30) not null,
tel_personal integer(9) not null,
tel_fix integer(9),
salari double not null,
num integer,
primary key (dni),
FOREIGN KEY (num) REFERENCES sala(num)
);

drop table if exists `client`;
create table `client` (
dni varchar(9),
nom varchar(30) not null,
cognom varchar(30) not null,
telefon integer(9) not null,
email varchar (50) not null,
sexe char check (sexe in('H','D')),
data_neixement date not null,
usuari varchar(20) not null,
contrasenya varchar(250) not null,
compte_bancari varchar(24) not null,
condicio varchar(150),
comunicacio_comercial boolean,
primary key (dni)
);

drop table if exists reserva_colectiva;
create table reserva_colectiva (
id int auto_increment,
`data` date,
hora time,
anulada boolean,
dni varchar(9),
id_act int,
FOREIGN KEY (id_act) REFERENCES activitat(id),
FOREIGN KEY (dni) REFERENCES `client`(dni),
primary key (id)
);

drop table if exists reserva_lliure;
create table reserva_lliure (
id int auto_increment,
`data`date,
hora time,
anulada boolean,
dni varchar(9),
id_act int,
primary key (id),
FOREIGN KEY (id_act) REFERENCES activitat(id),
FOREIGN KEY (dni) REFERENCES `client`(dni)
);

drop table if exists alta;
create table alta (
id int auto_increment,
primary key (id)
);

drop table if exists es_dona;
create table es_dona (
data_alta date not null,
data_baixa date default null,
dni varchar(9),
id int,
FOREIGN KEY (id) REFERENCES alta(id),
FOREIGN KEY (dni) REFERENCES `client`(dni)
);

drop table if exists curses;
create table curses (
id integer auto_increment,
nom varchar(255) not null,
primary key(id)
);

drop table if exists inscriu;
create table inscriu (
`data_cursa` date,
id int,
dni varchar(9),
primary key(id), 
FOREIGN KEY (id) REFERENCES curses(id),
FOREIGN KEY (dni) REFERENCES `client`(dni)
);

INSERT INTO client VALUES ('1234567L', 'Roger', 'Marin', 655656566, 'rmarin@gmail.com', 'H', '2003-9-5', 'rmarin', md5('1234'), '1234567B', '', false);
INSERT INTO client VALUES ('828556oP', 'Pedro', 'Picapiedra', 525896548, 'ppiedra@gmail.com', 'H', '1995-4-12', 'ppp', md5('1234'), 'ES522589654', '', false);

INSERT INTO activitat (nom, descripcio, color, durada_sessio) VALUES 
	('Body pump', '', 'vermell', 60),
	('Cycling', '', 'vermell', 45),
    ('Zumba', '', 'blau',  30),
    ('Pilates', '', 'blau',  30),
    ('Yoga', '', 'taronja',  45),
    ('Body Combat', '', 'verd',  45),
    ('Crossfit', '', 'taronja',  60),
    ('Body balance', '', 'verd',  60);

INSERT INTO activitat_lliure VALUES (2),
	(4),
    (5),
    (6);

INSERT INTO activitat_colectiva VALUES (1),
	(3),
    (7),
    (8);
    
INSERT INTO sala VALUES (10, '', 30),
	(11, '', 15),
    (12, '', 15),
    (13, '', 20),
    (14, '', 10),
    (15, '', 15),
    (16, '', 30),
    (17, '', 20);

INSERT INTO monitor VALUES ('1234567L', 'Ramon', 'Martinez', 674853448, 357375375, 2000, 10),
	('5687549R', 'Marta', 'Ruiz', 694283383, 846864648, 1400, 11),
    ('6865456W', 'Raquel', 'Puig', 678509465, 947479479, 2300, 12),
    ('5869584P', 'Laia', 'Alvarez', 639853745, 38697386, 1200, 13),
    ('5754345O', 'Xavi', 'Castells', 683275794, 794382575, 2300, 14),
    ('5568954L', 'Antoni', 'Pijuan', 865642975, 847597667, 2600, 15),
    ('4476345R', 'Eduard', 'Montero', 679474286, 583957453, 1800, 16),
    ('6786576P', 'Anna', 'Bofarull', 684733485, 679457956, 2400, 17);
    
INSERT INTO es_fa VALUES ('2022-2-28', '16:30', 10, 1),
	('2022-2-28', '18:30', 11, 2),
    ('2022-3-1', '16:30', 12, 3),
    ('2022-3-1', '18:30', 13, 4),
    ('2022-3-2', '16:30', 14, 5),
    ('2022-3-2', '18:30', 15, 6),
    ('2022-3-3', '16:30', 16, 7),
    ('2022-3-3', '18:30', 17, 8);
    
INSERT INTO alta VALUES (default);

INSERT INTO es_dona VALUES ('2022-2-27',null,'1234567L','1');

INSERT INTO reserva_lliure (`data`, hora, anulada, dni, id_act) VALUES ('2022-3-1', '16:30:00', null, '1234567L', '2');
INSERT INTO reserva_lliure (`data`, hora, anulada, dni, id_act) VALUES ('2022-2-28', '18:30:00', null, '1234567L', '2');

INSERT INTO reserva_colectiva (`data`, hora, anulada, dni, id_act) VALUES ('2022-3-1', '16:30:00', null, '1234567L', '1');
INSERT INTO reserva_colectiva (`data`, hora, anulada, dni, id_act) VALUES ('2022-4-1', '16:30:00', null, '1234567L', '3');


#####OBTENIR INFORMACIÓ DE LES ACTIVITATS LLIURES.
SELECT a.nom AS activitat, TIME_FORMAT(b.hora, '%H:%i') AS hora, c.num, c.aforament_max, d.nom, d.cognom
	FROM activitat a, es_fa b, sala c, monitor d
	WHERE a.id = b.id AND
    b.num = c.num AND
    d.num = c.num AND
    a.id IN
		(SELECT *
        FROM activitat_lliure);

#####OBTENIR INFORMACIÓ DE LES ACTIVITATS COL·LECTIVES.
SELECT a.nom AS activitat, TIME_FORMAT(b.hora, '%H:%i') AS hora, c.num, c.aforament_max, d.nom, d.cognom
	FROM activitat a, es_fa b, sala c, monitor d
	WHERE a.id = b.id AND
    b.num = c.num AND
    d.num = c.num AND
    a.id IN
		(SELECT *
        FROM activitat_colectiva);
    

    
#####VEURE SI EL CLIENT TÉ ALGUNA RESERVA LLIURE PENDENT.
#####SI RETORNA ALGUN VALOR VOL DIR QUE EL CLIENT TÉ ALGUNA RESERVA LLIURE I PER TANT NO PODRÀ FER-NE UNA ALTRA.
SELECT d.id
FROM client a, reserva_lliure b, activitat c, activitat_lliure d
WHERE a.dni = b.dni AND
b.id = c.id AND
c.id = d.id AND
b.data > curdate() AND
b.anulada is null;

#####VEURE SI EL CLIENT TÉ ALGUNA RESERVA COLECTIVA PENDENT.
#####SI RETORNA ALGUN VALOR VOL DIR QUE EL CLIENT TÉ ALGUNA RESERVA COLECTIVA I PER TANT NO PODRÀ FER-NE UNA ALTRA.
SELECT d.id
FROM client a, reserva_colectiva b, activitat c, activitat_colectiva d
WHERE a.dni = b.dni AND
b.id = c.id AND
c.id = d.id AND
b.data > curdate() AND
b.anulada is null AND
a.dni = '1234567L';

#####COMPROVAR SI HI HA AFORAMENT SUFICIENT PER FER LA RESERVA.
##S'HA DE PASSAR PER PARÀMETRE L'ID DE L'ACTIVITAT.
#SI RETORNA 0 L'AFORAMENT ESTÀ PLE I NO ES FA LA RESERVA.
SELECT c.aforament_max
FROM activitat a, es_fa b, sala c
WHERE a.id = b.id AND
b.num = c.num AND
b.data > curdate();

##OBTENIR DATA ALTA D'UN CLIENT
SELECT data_alta
FROM client a, es_dona b
WHERE a.dni = b.dni AND
data_baixa is null AND
a.dni = '1234567L';

#OBTENIR INFO RESERVES PENDENTS
SELECT a.nom AS activitat, TIME_FORMAT(b.hora, '%H:%i') AS hora, c.num, c.aforament_max, d.nom, d.cognom
                FROM activitat a, es_fa b, sala c, monitor d, reserva_lliure e, client f
                WHERE a.id = b.id AND
                b.num = c.num AND
                d.num = c.num AND
                e.id = a.id AND
                e.dni = f.dni AND
                b.data > curdate() AND
                a.id IN
                    (SELECT *
                    FROM activitat_lliure);

SELECT a.nom AS activitat, TIME_FORMAT(b.hora, '%H:%i') AS hora, c.num, c.aforament_max, d.nom, d.cognom
FROM activitat a, es_fa b, sala c, monitor d, reserva_lliure e, `client` f
WHERE a.id = e.id AND
a.id = b.id AND
b.num = c.num AND
d.num = c.num AND
e.dni = f.dni AND
a.id IN
	(SELECT *
	FROM activitat_lliure);      