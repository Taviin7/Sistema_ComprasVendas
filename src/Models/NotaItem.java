package Models;

public class NotaItem {
    private int id;
    private Nota nota;
    private Produto produto;
    private int quantidade;
    private float preco;

    public NotaItem() {
    }

    public NotaItem(int id, Nota nota, Produto produto, int quantidade, float preco) {
        this.id = id;
        this.nota = nota;
        this.produto = produto;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Nota getNota() {
        return nota;
    }

    public void setNota(Nota nota) {
        this.nota = nota;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public float calcularSubtotal() {
        return quantidade * preco;
    }

    @Override
    public String toString() {
        return "NotaItem{" +
                "id=" + id +
                ", produto=" + (produto != null ? produto.getNome() : "N/A") +
                ", quantidade=" + quantidade +
                ", preco=" + preco +
                ", subtotal=" + calcularSubtotal() +
                '}';
    }
}