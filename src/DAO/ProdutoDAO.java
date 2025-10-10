/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Connection.Conexao;
import Models.Produto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author 2830482411045
 */
public class ProdutoDAO {

    private Conexao conexao;
    private Connection conn;

    public ProdutoDAO() {
        this.conexao = new Conexao();
        this.conn = this.conexao.getConexao();
    }

    public void inserirProduto(Produto produto) {
        String query = "INSERT INTO Produto(pro_nome, pro_descricao, pro_precoUnitario, pro_qntdEstoque "
                + " VALUES(?, ?, ?, ?);";

        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setString(3, Float.toString(produto.getPrecoUnitario()));
            stmt.setString(4, Integer.toString(produto.getEstoque()));

            stmt.execute();
            JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir Produto: " + ex.getMessage());
        }
    }//fim do inserir

    public Produto getproduto(int id) {
        String query = "SELECT * FROM Produto WHERE pro_id = ?;";

        try {
            PreparedStatement stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            Produto p = new Produto();
            rs.first();
            p.setId(id);
            p.setNome(rs.getString("pro_nome"));
            p.setDescricao(rs.getString("pro_descricao"));
            p.setPrecoUnitario(rs.getFloat("pro_precoUnitario"));
            p.setEstoque(rs.getInt("pro_qntdEstoque"));

            return p;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar Produto: " + ex.getMessage());
            return null;
        }

    }//fim do getProduto

    //Lista para a tabela do relatório
    public List<Produto> listarProdutos() {
        String query = "SELECT * FROM Produto;";

        try {
            PreparedStatement stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            ResultSet rs = stmt.executeQuery();//obtenho o retorno da consulta e armazena no resultset
            List<Produto> listaProdutos = new ArrayList();//preparo uma lista de ojetos que vou armazenar a consulta

            //percorre o rs e salva as informações dentro de um objeto Cliente e depois add na lista
            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("pro_id"));
                p.setNome(rs.getString("pro_nome"));
                p.setDescricao(rs.getString("pro_descricao"));
                p.setPrecoUnitario(rs.getFloat("pro_precoUnitario"));
                p.setEstoque(rs.getInt("pro_qntdEstoque"));
                listaProdutos.add(p);
            }
            return listaProdutos;

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao consultar todos os Produto: " + ex.getMessage());
            return null;
        }

    }//fim do listarProduto

    public Produto editarProduto(Produto produto) {
        String query = "UPDATE Produto SET pro_nome = ?, pro_descricao = ?, pro_precoUnitario = ?, pro_qntdEstoque = ?"
                + " WHERE pro_id = ?;";

        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setString(3, Float.toString(produto.getPrecoUnitario()));
            stmt.setString(4, Integer.toString(produto.getEstoque()));
            stmt.setString(5, Integer.toString(produto.getId()));

            stmt.execute();
            JOptionPane.showMessageDialog(null, "Dados do produto atualizados com sucesso!");
            return produto;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao atulizar os dados do Produto: " + ex.getMessage());
            return null;
        }
    }//fim do editarProduto

    public void excluirProduto(int id) {
        try {
            String sql = "DELETE FROM Produto WHERE pro_id = ?;";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.execute();

            JOptionPane.showMessageDialog(null, "Produto exlcluido com sucesso!");

        } catch (SQLException ex) {
            System.out.println("Erro ao excluir Produto: " + ex.getMessage());
        }
    }//fim do excluirCliente
}
