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