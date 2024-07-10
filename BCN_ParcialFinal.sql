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

