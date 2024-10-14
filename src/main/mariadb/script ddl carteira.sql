USE Carteira;

DROP TABLE IF EXISTS Aporte;
DROP TABLE IF EXISTS TituloPublico;
DROP TABLE IF EXISTS Acao;
DROP TABLE IF EXISTS Setor;

CREATE TABLE Setor
(
    Id INT NOT NULL AUTO_INCREMENT,
    Descricao VARCHAR(60) NOT NULL,
    DataRegistroCriacao DATETIME NOT NULL,
    DataRegistroEdicao DATETIME NULL,
    PRIMARY KEY(Id)
);

CREATE TABLE Acao
(
    Id INT NOT NULL AUTO_INCREMENT,
    SetorId INT NOT NULL,
    RazaoSocial VARCHAR(100) NOT NULL,
    Ticker VARCHAR(10) NOT NULL,
    EhFIIs BIT NOT NULL DEFAULT 0,
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
    PrecoAjustado DECIMAL NOT NULL,
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
    TituloPublico INT,
    Preco DECIMAL NOT NULL,
    Quantidade INT NOT NULL,
    Movimentacao CHAR(1) NOT NULL, -- C - Compra V - Venda
    DataRegistroCriacao DATETIME NOT NULL,
    DataRegistroEdicao DATETIME NULL,
    DataRegistroRemocao DATETIME NULL,
    PRIMARY KEY(Id),
    CONSTRAINT `FK_Aporte_Acao` FOREIGN KEY (AcaoId) REFERENCES Acao(Id),
    CONSTRAINT `FK_Aporte_TituloPublico` FOREIGN KEY (TituloPublico) REFERENCES TituloPublico(Id)
)

