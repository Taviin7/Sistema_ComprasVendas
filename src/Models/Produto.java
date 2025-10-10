package Models;

/**
 * Classe Produto - ATUALIZADA com métodos de estoque
 * @author 2830482411045
 */
public class Produto {
    private int id;
    private String nome;
    private String descricao;
    private float precoUnitario;
    private int estoque;

    public Produto() {
    }

    public Produto(int id, String nome, String descricao, float precoUnitario, int estoque) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.precoUnitario = precoUnitario;
        this.estoque = estoque;
    }

    // Getters e Setters originais
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public float getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(float precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    // ========== MÉTODOS ADICIONAIS PARA GESTÃO DE ESTOQUE ==========

    /**
     * Verifica se há estoque disponível
     */
    public boolean temEstoqueDisponivel(int quantidadeDesejada) {
        return this.estoque >= quantidadeDesejada;
    }

    /**
     * Aumenta o estoque (usado em compras)
     */
    public void aumentarEstoque(int quantidade) {
        this.estoque += quantidade;
    }

    /**
     * Diminui o estoque (usado em vendas)
     */
    public void diminuirEstoque(int quantidade) {
        if (this.estoque >= quantidade) {
            this.estoque -= quantidade;
        }
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", precoUnitario=" + precoUnitario +
                ", estoque=" + estoque +
                '}';
    }
}