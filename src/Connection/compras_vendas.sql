-- =====================================================
-- BANCO DE DADOS: compras_vendas (versão persistente)
-- =====================================================

CREATE DATABASE IF NOT EXISTS compras_vendas CHARACTER SET utf8;
USE compras_vendas;

-- =====================================================
-- TABELA: Cliente
-- =====================================================
CREATE TABLE IF NOT EXISTS Cliente (
  cli_id INT NOT NULL AUTO_INCREMENT,
  cli_nome VARCHAR(45),
  cli_rua VARCHAR(45),
  cli_bairro VARCHAR(45),
  cli_cidade VARCHAR(45),
  cli_cep VARCHAR(9),
  cli_uf VARCHAR(2),
  cli_email VARCHAR(60),
  cli_telefone VARCHAR(12),
  cli_numero VARCHAR(10),
  PRIMARY KEY (cli_id)
) ENGINE=InnoDB;

-- =====================================================
-- TABELA: Produto
-- =====================================================
CREATE TABLE IF NOT EXISTS Produto (
  pro_id INT NOT NULL AUTO_INCREMENT,
  pro_nome VARCHAR(45),
  pro_descricao VARCHAR(100),
  pro_precoUnitario DOUBLE,
  pro_qntdEstoque INT,
  PRIMARY KEY (pro_id)
) ENGINE=InnoDB;

-- =====================================================
-- TABELA: Fornecedor
-- =====================================================
CREATE TABLE IF NOT EXISTS Fornecedor (
  for_id INT NOT NULL AUTO_INCREMENT,
  for_nome VARCHAR(45),
  for_nomeFantasia VARCHAR(45),
  for_cnpj VARCHAR(18),
  for_rua VARCHAR(60),
  for_bairro VARCHAR(60),
  for_cidade VARCHAR(45),
  for_cep VARCHAR(9),
  for_uf VARCHAR(2),
  for_email VARCHAR(60),
  for_telefone VARCHAR(12),
  for_numero VARCHAR(45),
  PRIMARY KEY (for_id)
) ENGINE=InnoDB;

-- =====================================================
-- TABELA: Notas
-- =====================================================
CREATE TABLE IF NOT EXISTS Notas (
  nts_id INT NOT NULL AUTO_INCREMENT,
  nts_data DATE,
  cli_id INT,
  for_id INT,
  PRIMARY KEY (nts_id),
  FOREIGN KEY (cli_id) REFERENCES Cliente (cli_id)
    ON DELETE SET NULL ON UPDATE CASCADE,
  FOREIGN KEY (for_id) REFERENCES Fornecedor (for_id)
    ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- TABELA: Notas_Item
-- =====================================================
CREATE TABLE IF NOT EXISTS Notas_Item (
  nti_id INT NOT NULL AUTO_INCREMENT,
  pro_id INT NOT NULL,
  nts_id INT NOT NULL,
  nti_qntd INT,
  nti_preco DOUBLE,
  PRIMARY KEY (nti_id),
  FOREIGN KEY (pro_id) REFERENCES Produto (pro_id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (nts_id) REFERENCES Notas (nts_id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- POPULAÇÃO DAS TABELAS
-- =====================================================

-- CLIENTES
INSERT INTO Cliente (cli_nome, cli_rua, cli_bairro, cli_cidade, cli_cep, cli_uf, cli_email, cli_telefone, cli_numero)
VALUES
('João Silva', 'Rua das Flores', 'Centro', 'São Paulo', '01000-000', 'SP', 'joao@gmail.com', '11987654321', '100'),
('Maria Souza', 'Av. Paulista', 'Bela Vista', 'São Paulo', '01310-000', 'SP', 'maria.souza@email.com', '11988887777', '120'),
('Carlos Pereira', 'Rua A', 'Jardim América', 'Campinas', '13000-000', 'SP', 'carlos.p@gmail.com', '19999998888', '45');

-- FORNECEDORES
INSERT INTO Fornecedor (for_nome, for_nomeFantasia, for_cnpj, for_rua, for_bairro, for_cidade, for_cep, for_uf, for_email, for_telefone, for_numero)
VALUES
('Tech Distribuidora LTDA', 'Tech Distrib', '12.345.678/0001-90', 'Rua da Tecnologia', 'Industrial', 'São Paulo', '04567-000', 'SP', 'contato@techdist.com', '1133334444', '200'),
('Alimentos Silva ME', 'Silva Foods', '98.765.432/0001-10', 'Av. dos Alimentos', 'Centro', 'Campinas', '13045-000', 'SP', 'vendas@silvafoods.com', '1922223333', '300');

-- PRODUTOS
INSERT INTO Produto (pro_nome, pro_descricao, pro_precoUnitario, pro_qntdEstoque)
VALUES
('Mouse Gamer RGB', 'Mouse com sensor óptico e iluminação RGB', 120.00, 50),
('Teclado Mecânico', 'Teclado com switches azuis e LED branco', 280.00, 30),
('Monitor 27" Full HD', 'Monitor com taxa de atualização de 75Hz', 950.00, 15),
('Cabo HDMI 2.1', 'Cabo de 1,5m compatível com 8K', 45.00, 100);

-- NOTAS
INSERT INTO Notas (nts_data, cli_id, for_id)
VALUES
('2025-10-01', 1, 1),
('2025-10-02', 2, 2);

-- NOTAS ITEM
INSERT INTO Notas_Item (pro_id, nts_id, nti_qntd, nti_precoVenda)
VALUES
(1, 1, 2, 120.00),
(2, 1, 1, 280.00),
(4, 2, 5, 45.00);

-- =====================================================
-- CONSULTAS DE TESTE
-- =====================================================
SELECT * FROM Cliente;
SELECT * FROM Fornecedor;
SELECT * FROM Produto;
SELECT * FROM Notas;
SELECT * FROM Notas_Item;