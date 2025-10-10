/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Connection.Conexao;
import Models.Nota;
import Models.NotaItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author 2830482411045
 */
public class NotaDAO {

    private Conexao conexao;
    private Connection conn;

    public NotaDAO() {
        this.conexao = new Conexao();
        this.conn = this.conexao.getConexao();
    }

    public void inserirNota(Nota nota) throws SQLException {
        String query = "INSERT INTO Notas(nts_data, cli_id, for_id) VALUES (?, ?, ?);";

        try {
            PreparedStatement stmt = this.conn.prepareStatement(query);
            stmt.setDate(1, new java.sql.Date(nota.getData().getTime()));
            stmt.setInt(2, nota.getCliente().getID());
            stmt.setInt(3, nota.getFornecedor().getID());
            stmt.executeUpdate();

            stmt.execute();
            JOptionPane.showMessageDialog(null, "Nota cadastrado com sucesso!");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao inserir Nota:" + ex.getMessage());
        }

        // Salva os itens usando o NotaItemDAO
        NotaItemDAO itemDAO = new NotaItemDAO();
        for (NotaItem item
                : nota.getItens()) {
            item.setNota(nota);
            itemDAO.salvarNotaItem(item);
        }
        conn.close();

    }
}