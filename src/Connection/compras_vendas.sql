-- =====================================================
-- BANCO DE DADOS: compras_vendas (VERSÃO SIMPLES)
-- =====================================================

CREATE DATABASE IF NOT EXISTS compras_vendas CHARACTER SET utf8mb4;
USE compras_vendas;
-- drop database compras_vendas;

-- =====================================================
-- TABELA: Cliente
-- =====================================================
CREATE TABLE IF NOT EXISTS Cliente (
  cli_id INT NOT NULL AUTO_INCREMENT,
  cli_nome VARCHAR(100) NOT NULL,
  cli_rua VARCHAR(100),
  cli_bairro VARCHAR(100),
  cli_cidade VARCHAR(100),
  cli_cep VARCHAR(9),
  cli_uf VARCHAR(2),
  cli_email VARCHAR(100),
  cli_telefone VARCHAR(15),
  cli_numero VARCHAR(10),
  PRIMARY KEY (cli_id)
) ENGINE=InnoDB;

-- =====================================================
-- TABELA: Fornecedor
-- =====================================================
CREATE TABLE IF NOT EXISTS Fornecedor (
  for_id INT NOT NULL AUTO_INCREMENT,
  for_nome VARCHAR(100) NOT NULL,
  for_nomeFantasia VARCHAR(100),
  for_cnpj VARCHAR(18),
  for_rua VARCHAR(100),
  for_bairro VARCHAR(100),
  for_cidade VARCHAR(100),
  for_cep VARCHAR(9),
  for_uf VARCHAR(2),
  for_email VARCHAR(100),
  for_telefone VARCHAR(15),
  for_numero VARCHAR(10),
  PRIMARY KEY (for_id)
) ENGINE=InnoDB;

-- =====================================================
-- TABELA: Produto
-- =====================================================
CREATE TABLE IF NOT EXISTS Produto (
  pro_id INT NOT NULL AUTO_INCREMENT,
  pro_nome VARCHAR(100) NOT NULL,
  pro_descricao VARCHAR(255),
  pro_precoUnitario DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  pro_qntdEstoque INT NOT NULL DEFAULT 0,
  PRIMARY KEY (pro_id)
) ENGINE=InnoDB;

-- =====================================================
-- TABELA: Notas
-- =====================================================
CREATE TABLE IF NOT EXISTS Notas (
  nts_id INT NOT NULL AUTO_INCREMENT,
  nts_data DATE NOT NULL,
  nts_tipo CHAR(1) NOT NULL DEFAULT 'E' COMMENT 'E=Entrada(Compra), S=Saída(Venda)',
  cli_id INT NULL,
  for_id INT NULL,
  PRIMARY KEY (nts_id),
  FOREIGN KEY (cli_id) REFERENCES Cliente (cli_id) ON DELETE SET NULL ON UPDATE CASCADE,
  FOREIGN KEY (for_id) REFERENCES Fornecedor (for_id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- TABELA: Notas_Item
-- =====================================================
CREATE TABLE IF NOT EXISTS Notas_Item (
  nti_id INT NOT NULL AUTO_INCREMENT,
  pro_id INT NOT NULL,
  nts_id INT NOT NULL,
  nti_qntd INT NOT NULL DEFAULT 1,
  nti_preco DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (nti_id),
  FOREIGN KEY (pro_id) REFERENCES Produto (pro_id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (nts_id) REFERENCES Notas (nts_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- =====================================================
-- DADOS DE EXEMPLO
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

-- NOTAS DE ENTRADA (Compras)
INSERT INTO Notas (nts_data, nts_tipo, cli_id, for_id)
VALUES
('2025-10-01', 'E', NULL, 1),
('2025-10-03', 'E', NULL, 2);

-- NOTAS DE SAÍDA (Vendas)
INSERT INTO Notas (nts_data, nts_tipo, cli_id, for_id)
VALUES
('2025-10-05', 'S', 1, NULL),
('2025-10-07', 'S', 2, NULL);

-- ITENS DAS NOTAS
INSERT INTO Notas_Item (pro_id, nts_id, nti_qntd, nti_preco)
VALUES
-- Nota 1: Compra
(1, 1, 50, 90.00),
(2, 1, 30, 210.00),

-- Nota 2: Compra
(3, 2, 15, 750.00),
(4, 2, 100, 32.00),

-- Nota 3: Venda
(1, 3, 2, 120.00),
(2, 3, 1, 280.00),

-- Nota 4: Venda
(4, 4, 5, 45.00);

-- =====================================================
-- CONSULTAS ÚTEIS
-- =====================================================

-- Ver todas as notas
SELECT 
    n.nts_id AS codigo,
    n.nts_data AS data,
    CASE n.nts_tipo 
        WHEN 'E' THEN 'ENTRADA'
        WHEN 'S' THEN 'SAÍDA'
    END AS tipo,
    COALESCE(c.cli_nome, f.for_nome) AS cliente_fornecedor
FROM Notas n
LEFT JOIN Cliente c ON n.cli_id = c.cli_id
LEFT JOIN Fornecedor f ON n.for_id = f.for_id
ORDER BY n.nts_data DESC;

-- Ver estoque atual
SELECT 
    pro_id,
    pro_nome,
    pro_qntdEstoque AS estoque,
    pro_precoUnitario AS preco
FROM Produto;

-- Ver itens de uma nota específica
SELECT 
    p.pro_nome AS produto,
    ni.nti_qntd AS quantidade,
    ni.nti_preco AS preco_unitario,
    (ni.nti_qntd * ni.nti_preco) AS subtotal
FROM Notas_Item ni
INNER JOIN Produto p ON ni.pro_id = p.pro_id
WHERE ni.nts_id = 1;

SELECT * FROM Cliente;
SELECT * FROM Fornecedor;
SELECT * FROM Produto;
SELECT * FROM Notas;
SELECT * FROM Notas_Item;

-- test