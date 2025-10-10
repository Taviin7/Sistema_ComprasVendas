/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connection;

import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author 2830482411045
 */
public class Conexao {
     public java.sql.Connection getConexao(){
        try {
            java.sql.Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/compras_vendas?useTimezone=true&serverTimezone=UTC",
                                                           "root", 
                                                           "root"); /*url, usuario senha */
            
            System.out.println("Conexão realizada com sucesso!");
            return conn;
        }
        catch(Exception e){
            System.out.println("Erro ao conectar no BD" + e.getMessage());
            return null;
        }
    }
}
