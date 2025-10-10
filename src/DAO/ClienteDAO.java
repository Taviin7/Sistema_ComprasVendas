/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Connection.Conexao;
import Models.Cliente;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author 2830482411045
 */
public class ClienteDAO {

    private Conexao conexao;
    private Connection conn;

    public ClienteDAO() {
        this.conexao = new Conexao();
        this.conn = this.conexao.getConexao();
    }

    public void inserirCliente(Cliente cliente) {
        String query = "INSERT INTO Cliente(cli_nome, cli_rua, cli_numero, cli_bairro, cli_cidade, cli_cep , cli_uf, "
                + "cli_email, cli_telefone)"
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getRua());
            stmt.setString(3, cliente.getNumero());
            stmt.setString(4, cliente.getBairro());
            stmt.setString(5, cliente.getCidade());
            stmt.setString(6, cliente.getCEP());
            stmt.setString(7, cliente.getUF());
            stmt.setString(8, cliente.getEmail());
            stmt.setString(9, cliente.getTelefone());

            stmt.execute();
            JOptionPane.showMessageDialog(null, "Cliente cadastrado com sucesso!");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir Cliente:" + ex.getMessage());
        }
    }//fim do inserir

    public Cliente getCliente(int id) {
        String query = "SELECT * FROM Cliente WHERE cli_id = ?;";

        try {
            PreparedStatement stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Cliente c = new Cliente();
            rs.first();
            c.setID(id);
            c.setNome(rs.getString("cli_nome"));
            c.setRua(rs.getString("cli_rua"));
            c.setNumero(rs.getString("cli_numero"));
            c.setBairro(rs.getString("cli_bairro"));
            c.setCidade(rs.getString("cli_cidade"));
            c.setCEP(rs.getString("cli_cep"));
            c.setUF(rs.getString("cli_uf"));
            c.setEmail(rs.getString("cli_email"));
            c.setTelefone(rs.getString("cli_telefone"));

            return c;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Cliente: " + ex.getMessage());
            return null;
        }

    }//fim do getCliente

    //Lista para a tabela do relatório
    public List<Cliente> listarClientes() {
        String query = "SELECT * FROM Cliente;";

        try {
            PreparedStatement stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = stmt.executeQuery();//obtenho o retorno da consulta e armazena no resultset
            List<Cliente> listaClientes = new ArrayList();//preparo uma lista de ojetos que vou armazenar a consulta

            //percorre o rs e salva as informações dentro de um objeto Cliente e depois add na lista
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setID(rs.getInt("cli_id"));
                c.setNome(rs.getString("cli_nome"));
                c.setRua(rs.getString("cli_rua"));
                c.setNumero(rs.getString("cli_numero"));
                c.setBairro(rs.getString("cli_bairro"));
                c.setCidade(rs.getString("cli_cidade"));
                c.setCEP(rs.getString("cli_cep"));
                c.setUF(rs.getString("cli_uf"));
                c.setEmail(rs.getString("cli_email"));
                c.setTelefone(rs.getString("cli_telefone"));
                listaClientes.add(c);
            }
            return listaClientes;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar todos os Cliente: " + ex.getMessage());
            return null;
        }

    }//fim do listarClientes

    public Cliente editarCliente(Cliente cliente) {
        String query = "UPDATE Cliente SET cli_nome = ?, cli_rua = ?, cli_numero = ?, cli_bairro = ?, cli_cidade = ?,"
                + " cli_cep = ?, cli_uf = ?, cli_email = ?, cli_telefone = ? WHERE cli_id = ?;";

        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getRua());
            stmt.setString(3, cliente.getNumero());
            stmt.setString(4, cliente.getBairro());
            stmt.setString(5, cliente.getCidade());
            stmt.setString(6, cliente.getCEP());
            stmt.setString(7, cliente.getUF());
            stmt.setString(8, cliente.getEmail());
            stmt.setString(9, cliente.getTelefone());
            stmt.setInt(10, cliente.getID());

            stmt.execute();
            JOptionPane.showMessageDialog(null, "Dados do cliente atualizados com sucesso!");
            return cliente;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao atulizar os dados do Cliente: " + ex.getMessage());
            return null;
        }
    }//fim do editarCliente

    public void excluirCliente(int id) {
        try {
            String sql = "DELETE FROM Cliente WHERE cli_id = ?;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();

            JOptionPane.showMessageDialog(null, "Cliente exlcluido com sucesso!");

        } catch (SQLException ex) {
            System.out.println("Erro ao excluir Cliente: " + ex.getMessage());
        }
    }//fim do excluirCliente
}
