/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import javax.swing.JTextField;

/**
 *
 * @author 2830482411045
 */
public class Cliente {
    private int id;
    private String nome;
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String cep;
    private String uf;
    private String email;
    private String telefone;
    
    //Getters
    public int getID() {
        return id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public String getRua() {
        return rua;
    }
    
    public String getNumero() {
        return numero;
    }
    
    public String getBairro() {
        return bairro;
    }
    
    public String getCidade() {
        return cidade;
    }
    
    public String getCEP() {
        return cep;
    }
    
    public String getUF() {
        return uf;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    //Setters
    public void setID(int id) {
        this.id = id;
    }
    
    public void setNome(String nome){
        this.nome = nome;
    }
    
    public void setRua(String rua){
        this.rua = rua;
    }
    
    public void setNumero(String numero){
        this.numero = numero;
    }
    
    public void setBairro(String bairro){
        this.bairro = bairro;
    }
    
    public void setCidade(String cidade){
        this.cidade = cidade;
    }
    
    public void setCEP(String cep){
        this.cep = cep;
    }
    
    public void setUF(String uf){
        this.uf = uf;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
    public void setTelefone(String telefone){
        this.telefone = telefone;
    }

}
