package Models;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Nota {
    private int id;
    private Date data;
    private String tipo; // "E" = Entrada (Compra), "S" = Saída (Venda)
    private Cliente cliente;
    private Fornecedor fornecedor;
    private List<NotaItem> itens;

    public Nota() {
        this.itens = new ArrayList<>();
    }

    public Nota(int id, Date data, String tipo, Cliente cliente, Fornecedor fornecedor) {
        this.id = id;
        this.data = data;
        this.tipo = tipo;
        this.cliente = cliente;
        this.fornecedor = fornecedor;
        this.itens = new ArrayList<>();
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public List<NotaItem> getItens() {
        return itens;
    }

    public void setItens(List<NotaItem> itens) {
        this.itens = itens;
    }

    public void adicionarItem(NotaItem item) {
        this.itens.add(item);
        item.setNota(this);
    }

    public double calcularTotal() {
        return itens.stream()
                .mapToDouble(item -> item.getPreco() * item.getQuantidade())
                .sum();
    }

    /**
     * Verifica se é uma nota de entrada (compra)
     */
    public boolean isEntrada() {
        return "E".equalsIgnoreCase(this.tipo);
    }

    /**
     * Verifica se é uma nota de saída (venda)
     */
    public boolean isSaida() {
        return "S".equalsIgnoreCase(this.tipo);
    }

    /**
     * Retorna a descrição do tipo
     */
    public String getTipoDescricao() {
        if ("E".equalsIgnoreCase(this.tipo)) {
            return "ENTRADA (Compra)";
        } else if ("S".equalsIgnoreCase(this.tipo)) {
            return "SAÍDA (Venda)";
        }
        return "Desconhecido";
    }

    @Override
    public String toString() {
        return "Nota{" +
                "id=" + id +
                ", data=" + data +
                ", tipo=" + getTipoDescricao() +
                ", cliente=" + (cliente != null ? cliente.getNome() : "N/A") +
                ", fornecedor=" + (fornecedor != null ? fornecedor.getNome() : "N/A") +
                ", total=" + calcularTotal() +
                '}';
    }
}
