CREATE DATABASE BCN_ParcialFinal

USE BCN_ParcialFinal

-- Creación de Tablas--

CREATE TABLE Cliente (
	ClienteID INT IDENTITY (1,1) PRIMARY KEY,
	NameCliente VARCHAR(65) NOT NULL,
	LastNameCliente VARCHAR (65) NOT NULL,
	Phone VARCHAR (25) NOT NULL,
	Direction VARCHAR (135) NOT NULL

);


CREATE TABLE Facilitator (
	FacilitatorID INT IDENTITY (1,1) PRIMARY KEY,
	FacilitatorName VARCHAR (65) NOT NULL
);

CREATE TABLE Cards (
	CardID INT IDENTITY(1,1) PRIMARY KEY,
	CardNumber VARCHAR(26) NOT NULL,
	DateExpired DATE NOT NULL,
	CardType VARCHAR (30) NOT NULL
);

CREATE TABLE Smart_Shopping (
	TransactionID INT IDENTITY(1,1) PRIMARY KEY,
	DateShopping DATE NOT NULL,
	TotalAmount DECIMAL (10 , 2) NOT NULL,
	DescriptionShopping VARCHAR(130) NOT NULL,
);



-- Alteración de tablas para relaciones --

ALTER TABLE Cards 
ADD ClienteID INT 
ALTER TABLE Cards 
ADD FacilitatorID INT
ALTER TABLE Smart_Shopping 
ADD CardID INT 

-- Foreign keys --

ALTER TABLE Cards
ADD CONSTRAINT FK_Clients FOREIGN KEY (ClienteID) REFERENCES Cliente (ClienteID) ON DELETE CASCADE
ALTER TABLE Cards 
ADD CONSTRAINT FK_Facilitator FOREIGN KEY (FacilitatorID) REFERENCES Facilitator (FacilitatorID)

ALTER TABLE Smart_Shopping 
ADD CONSTRAINT FK_Cards FOREIGN KEY (CardID) REFERENCES Cards(CardID) ON DELETE CASCADE 

SELECT * FROM Cliente

SELECT TransactionID, ce.ClienteID, ce.NameCliente, s.DateShopping, s.TotalAmount, SUM(s.TotalAmount) AS Gastos 
                    FROM Smart_Shopping s 
                    INNER JOIN Cards c ON c.CardID = s.CardID 
                    INNER JOIN Cliente ce ON ce.ClienteID = c.ClienteID 
					WHERE ce.ClienteID = 2 AND s.DateShopping BETWEEN '2024-07-16' AND '2024-07-19' 
					GROUP BY s.TransactionID, ce.ClienteID, ce.NameCliente, s.DateShopping, s.TotalAmount

SELECT SUM(s.TotalAmount) AS TotalGastado
FROM Smart_Shopping s
INNER JOIN Cards c ON c.CardID = s.CardID
INNER JOIN Cliente ce ON ce.ClienteID = c.ClienteID
WHERE ce.ClienteID = 2 AND MONTH(s.DateShopping) = '2024-07-16' AND YEAR(s.DateShopping) = '2024-07-19'

SELECT s.TransactionID, ce.ClienteID, ce.NameCliente, s.DateShopping, s.TotalAmount, 
       SUM(s.TotalAmount) OVER (PARTITION BY ce.ClienteID) AS Gastos 
FROM Smart_Shopping s 
INNER JOIN Cards c ON c.CardID = s.CardID 
INNER JOIN Cliente ce ON ce.ClienteID = c.ClienteID 
WHERE ce.ClienteID = 2 AND s.DateShopping BETWEEN '2024-07-16' AND '2024-07-19';

select * from Smart_Shopping
select * from Cards
select * from Facilitator

SELECT  ce.NameCliente, COUNT(s.TransactionID) AS CantidadCompras, 
                        SUM(s.TotalAmount) AS TotalGastado  
						FROM Smart_Shopping s  
                       INNER JOIN Cards c ON c.CardID = s.CardID 
                       INNER JOIN Cliente ce ON ce.ClienteID = c.ClienteID 
                       INNER JOIN Facilitator f ON f.FacilitatorID = c.FacilitatorID 
					   GROUP BY  ce.NameCliente
INSERT INTO Facilitator(FacilitatorName) VALUES
('Visa'),
('MasterCard'),
('American Express'),
('UnionPay'),
('Discover'),
('JCB')

INSERT INTO Cliente (NameCliente, LastNameCliente, Phone, Direction) VALUES
('Juan', 'Pérez', '123456789', 'Calle Falsa 123, Ciudad, País'),
('María', 'González', '987654321', 'Avenida Siempre Viva 456, Ciudad, País'),
('Carlos', 'Sánchez', '555555555', 'Boulevard del Sol 789, Ciudad, País'),
('Ana', 'Martínez', '444444444', 'Plaza Mayor 101, Ciudad, País'),
('Luis', 'Ramírez', '333333333', 'Calle del Río 202, Ciudad, País'),
('Elena', 'Hernández', '222222222', 'Calle de la Luna 303, Ciudad, País'),
('Pedro', 'López', '111111111', 'Avenida del Mar 404, Ciudad, País'),
('Sofía', 'Díaz', '999999999', 'Boulevard de los Sueños 505, Ciudad, País'),
('Miguel', 'Torres', '888888888', 'Plaza del Sol 606, Ciudad, País'),
('Lucía', 'Jiménez', '777777777', 'Calle de la Paz 707, Ciudad, País'),
('Diego', 'Ortiz', '666666666', 'Avenida de las Estrellas 808, Ciudad, País'),
('Clara', 'Moreno', '555555555', 'Boulevard de los Poetas 909, Ciudad, País'),
('Javier', 'Rojas', '444444444', 'Plaza de la Libertad 1010, Ciudad, País'),
('Paula', 'Navarro', '333333333', 'Calle de la Amistad 1111, Ciudad, País'),
('Alejandro', 'Flores', '222222222', 'Avenida del Bosque 1212, Ciudad, País');

INSERT INTO Cards (CardNumber, DateExpired, ClienteID, FacilitatorID) VALUES
('4539148803436467', '2024-07-31', 1, 1),  -- Visa
('4716593722734022', '2025-01-31', 2, 2),  -- MasterCard
('4485264350853018', '2023-12-31', 3, 3),  -- American Express
('4716793715373523', '2024-09-30', 4, 4),  -- UnionPay
('4485573891826142', '2025-05-31', 5, 5),  -- Discover
('4539579513978450', '2024-06-30', 6, 6),  -- JCB
('4716719482025847', '2025-03-31', 7, 1),  -- Visa
('4485925897340123', '2024-10-31', 8, 2),  -- MasterCard
('4539135284324856', '2025-11-30', 9, 3),  -- American Express
('4716439509275814', '2024-08-31', 10, 4), -- UnionPay
('4485381592658032', '2025-02-28', 11, 5), -- Discover
('4539296471984570', '2024-11-30', 12, 6), -- JCB
('4716159370821524', '2025-04-30', 13, 1), -- Visa
('4485395271480632', '2024-12-31', 14, 2), -- MasterCard
('4539142672593501', '2025-06-30', 15, 3); -- American Express

-- Suponiendo que los valores de CardID corresponden a registros existentes en la tabla Cards
INSERT INTO Smart_Shopping (DateShopping, TotalAmount, DescriptionShopping, CardID) VALUES
('2024-07-10', 150.00, 'Compra de víveres', 1),        -- Utilizando CardID 1 de la tabla Cards
('2024-07-11', 75.50, 'Ropa de temporada', 2),         -- Utilizando CardID 2 de la tabla Cards
('2024-07-12', 200.00, 'Electrodomésticos', 3),        -- Utilizando CardID 3 de la tabla Cards
('2024-07-13', 50.25, 'Artículos de papelería', 4),    -- Utilizando CardID 4 de la tabla Cards
('2024-07-14', 300.00, 'Muebles para el hogar', 5),    -- Utilizando CardID 5 de la tabla Cards
('2024-07-15', 120.75, 'Ropa deportiva', 6),           -- Utilizando CardID 6 de la tabla Cards
('2024-07-16', 80.50, 'Libros y revistas', 7),         -- Utilizando CardID 7 de la tabla Cards
('2024-07-17', 95.20, 'Accesorios electrónicos', 8),   -- Utilizando CardID 8 de la tabla Cards
('2024-07-18', 180.00, 'Juguetes para niños', 9),      -- Utilizando CardID 9 de la tabla Cards
('2024-07-19', 65.75, 'Cosméticos y belleza', 10),     -- Utilizando CardID 10 de la tabla Cards
('2024-07-20', 250.50, 'Equipos deportivos', 11),      -- Utilizando CardID 11 de la tabla Cards
('2024-07-21', 45.90, 'Artículos de jardinería', 12),  -- Utilizando CardID 12 de la tabla Cards
('2024-07-22', 150.25, 'Instrumentos musicales', 13),   -- Utilizando CardID 13 de la tabla Cards
('2024-07-23', 175.80, 'Material educativo', 14),      -- Utilizando CardID 14 de la tabla Cards
('2024-07-24', 90.00, 'Herramientas de bricolaje', 15); -- Utilizando CardID 15 de la tabla Cards

