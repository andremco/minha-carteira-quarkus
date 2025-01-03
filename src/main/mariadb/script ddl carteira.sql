USE Carteira;

DROP TABLE IF EXISTS Aporte;
DROP TABLE IF EXISTS TituloPublico;
DROP TABLE IF EXISTS Acao;
DROP TABLE IF EXISTS Setor;
DROP TABLE IF EXISTS TipoAtivo;

CREATE TABLE TipoAtivo(
                          Id INT NOT NULL AUTO_INCREMENT,
                          Descricao VARCHAR(60) NOT NULL,
                          DataRegistroCriacao DATETIME NOT NULL,
                          PRIMARY KEY(Id)
);

INSERT INTO TipoAtivo (Descricao, DataRegistroCriacao)
VALUES
    ('Ação', '2024-10-23'),
    ('Fundo Imobiliário', '2024-10-23'),
    ('Brazilian Depositary Receipts', '2024-10-23'),
    ('Título Público', '2024-10-23');

CREATE TABLE Setor
(
    Id INT NOT NULL AUTO_INCREMENT,
    TipoAtivoId INT NOT NULL,
    Descricao VARCHAR(60) NOT NULL,
    DataRegistroCriacao DATETIME NOT NULL,
    DataRegistroEdicao DATETIME NULL,
    PRIMARY KEY(Id),
    CONSTRAINT `FK_Setor_TipoAtivo` FOREIGN KEY (TipoAtivoId) REFERENCES TipoAtivo(Id)
);

CREATE TABLE Acao
(
    Id INT NOT NULL AUTO_INCREMENT,
    SetorId INT NOT NULL,
    RazaoSocial VARCHAR(100),
    Ticker VARCHAR(10) NOT NULL,
    Nota INT NOT NULL,
    DataRegistroCriacao DATETIME NOT NULL,
    DataRegistroEdicao DATETIME NULL,
    DataRegistroRemocao DATETIME NULL,
    PRIMARY KEY(Id),
    CONSTRAINT `FK_Acao_Setor` FOREIGN KEY (SetorId) REFERENCES Setor(Id)
);

CREATE TABLE TituloPublico
(
    Id INT NOT NULL AUTO_INCREMENT,
    SetorId INT NOT NULL,
    Descricao VARCHAR(100) NOT NULL,
    PrecoInicial DECIMAL(10,2) NOT NULL,
    Nota INT NOT NULL,
    DataRegistroCriacao DATETIME NOT NULL,
    DataRegistroEdicao DATETIME NULL,
    DataRegistroRemocao DATETIME NULL,
    PRIMARY KEY(Id),
    CONSTRAINT `FK_TituloPublico_Setor` FOREIGN KEY (SetorId) REFERENCES Setor(Id)
);

CREATE TABLE Aporte
(
    Id INT NOT NULL AUTO_INCREMENT,
    AcaoId INT,
    TituloPublicoId INT,
    Preco DECIMAL(10,2) NOT NULL,
    Quantidade INT NOT NULL,
    Movimentacao CHAR(1) NOT NULL, -- C - Compra V - Venda
    DataRegistroCriacao DATETIME NOT NULL,
    DataRegistroEdicao DATETIME NULL,
    DataRegistroRemocao DATETIME NULL,
    PRIMARY KEY(Id),
    CONSTRAINT `FK_Aporte_Acao` FOREIGN KEY (AcaoId) REFERENCES Acao(Id),
    CONSTRAINT `FK_Aporte_TituloPublico` FOREIGN KEY (TituloPublicoId) REFERENCES TituloPublico(Id)
);
