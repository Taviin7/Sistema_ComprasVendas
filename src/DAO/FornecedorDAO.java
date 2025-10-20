package DAO;

import Connection.Conexao;
import Models.Fornecedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/*
 * forck nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * forck nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author tavio
 */
public class FornecedorDAO {

    private Conexao conexao;
    private Connection conn;

    public FornecedorDAO() {
        this.conexao = new Conexao();
        this.conn = this.conexao.getConexao();
    }

    public void inserirFornecedor(Fornecedor fornecedor) {
        String query = "INSERT INTO Fornecedor(for_nome, for_nomeFantasia, for_cnpj, for_rua, for_numero, for_bairro, "
                + "for_cidade, for_cep , for_uf, for_email, for_telefone) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, fornecedor.getNome());
            stmt.setString(2, fornecedor.getNomeFantasia());
            stmt.setString(3, fornecedor.getCnpj());
            stmt.setString(4, fornecedor.getRua());
            stmt.setString(5, fornecedor.getNumero());
            stmt.setString(6, fornecedor.getBairro());
            stmt.setString(7, fornecedor.getCidade());
            stmt.setString(8, fornecedor.getCEP());
            stmt.setString(9, fornecedor.getUF());
            stmt.setString(10, fornecedor.getEmail());
            stmt.setString(11, fornecedor.getTelefone());

            stmt.execute();
            JOptionPane.showMessageDialog(null, "Fornecedor cadastrado com sucesso!");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir Fornecedor:" + ex.getMessage());
        }
    }//fim do inserir

    public Fornecedor getFornecedor(int id) {
        String query = "SELECT * FROM Fornecedor WHERE for_id = ?;";

        try {
            PreparedStatement stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Fornecedor f = new Fornecedor();
            rs.first();
            f.setID(id);
            f.setNome(rs.getString("for_nome"));
            f.setNomeFantasia(rs.getString("for_nomeFantasia"));
            f.setCnpj(rs.getString("for_cnpj"));
            f.setRua(rs.getString("for_rua"));
            f.setNumero(rs.getString("for_numero"));
            f.setBairro(rs.getString("for_bairro"));
            f.setCidade(rs.getString("for_cidade"));
            f.setCEP(rs.getString("for_cep"));
            f.setUF(rs.getString("for_uf"));
            f.setEmail(rs.getString("for_email"));
            f.setTelefone(rs.getString("for_telefone"));

            return f;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Fornecedor: " + ex.getMessage());
            return null;
        }

    }//fim do getfornecedor


    //Lista para a tabela do relatório
    public List<Fornecedor> listarFornecedores() {
        String query = "SELECT * FROM Fornecedor;";

        try {
            PreparedStatement stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = stmt.executeQuery();//obtenho o retorno da consulta e armazena no resultset
            List<Fornecedor> listaFornecedores = new ArrayList();//preparo uma lista de ojetos que vou armazenar a consulta

            //percorre o rs e salva as informações dentro de um objeto fornecedor e depois add na lista
            while (rs.next()) {
                Fornecedor f = new Fornecedor();
                f.setID(rs.getInt("for_id"));
                f.setNome(rs.getString("for_nome"));
                f.setNomeFantasia(rs.getString("for_nomeFantasia"));
                f.setCnpj(rs.getString("for_cnpj"));
                f.setRua(rs.getString("for_rua"));
                f.setNumero(rs.getString("for_numero"));
                f.setBairro(rs.getString("for_bairro"));
                f.setCidade(rs.getString("for_cidade"));
                f.setCEP(rs.getString("for_cep"));
                f.setUF(rs.getString("for_uf"));
                f.setEmail(rs.getString("for_email"));
                f.setTelefone(rs.getString("for_telefone"));
                listaFornecedores.add(f);
            }
            return listaFornecedores;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar todos os Fornecedor: " + ex.getMessage());
            return null;
        }

    }//fim do listarfornecedors

    public Fornecedor editarFornecedor(Fornecedor fornecedor) {
        String query = "UPDATE Fornecedor SET for_nome = ?, for_nomeFantasia = ?, for_cnpj = ?, for_rua = ?, for_numero = ?,"
                + " for_bairro = ?, for_cidade = ?, for_cep = ?, for_uf = ?, for_email = ?, for_telefone = ? WHERE for_id = ?;";

        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, fornecedor.getNome());
            stmt.setString(2, fornecedor.getNomeFantasia());
            stmt.setString(3, fornecedor.getCnpj());
            stmt.setString(4, fornecedor.getRua());
            stmt.setString(5, fornecedor.getNumero());
            stmt.setString(6, fornecedor.getBairro());
            stmt.setString(7, fornecedor.getCidade());
            stmt.setString(8, fornecedor.getCEP());
            stmt.setString(9, fornecedor.getUF());
            stmt.setString(10, fornecedor.getEmail());
            stmt.setString(11, fornecedor.getTelefone());
            stmt.setInt(12, fornecedor.getID());

            stmt.execute();
            JOptionPane.showMessageDialog(null, "Dados do Fornecedor atualizados com sucesso!");
            return fornecedor;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao atulizar os dados do Fornecedor: " + ex.getMessage());
            return null;
        }
    }//fim do editarfornecedor

    public void excluirFornecedor(int id) {
        try {
            String sql = "DELETE FROM Fornecedor WHERE for_id = ?;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();

            JOptionPane.showMessageDialog(null, "Fornecedor exlcluido com sucesso!");

        } catch (SQLException ex) {
            System.out.println("Erro ao excluir Fornecedor: " + ex.getMessage());
        }
    }//fim do excluirfornecedor
}
