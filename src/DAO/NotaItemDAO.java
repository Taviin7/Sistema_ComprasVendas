/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Connection.Conexao;
import Models.NotaItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author 2830482411045
 */
public class NotaItemDAO {

    private Conexao conexao;
    private Connection conn;

    public NotaItemDAO() {
        this.conexao = new Conexao();
        this.conn = this.conexao.getConexao();
    }

    public void salvarNotaItem(NotaItem item) throws SQLException {
        String query = "INSERT INTO Notas_Item (pro_id, nts_id, nti_qntd, nti_precoVenda) VALUES (?, ?, ?, ?);";

        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setInt(1, item.getProduto().getId());
            stmt.setInt(2, item.getNota().getId());
            stmt.setInt(3, item.getQuantidade());
            stmt.setDouble(4, item.getPrecoVenda());
            stmt.executeUpdate();

            stmt.execute();
            JOptionPane.showMessageDialog(null, "Item cadastrado na nota com sucesso!");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir item na Nota:" + ex.getMessage());
        }
    }
}