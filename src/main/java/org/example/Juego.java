package org.example;

public class Juego {
    private int _id;
    private String titulo;
    private String director;
    private double precio;

    public Juego() {
    }

    public Juego(int _id, String titulo, String director, double precio) {
        this._id = _id;
        this.titulo = titulo;
        this.director = director;
        this.precio = precio;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Juego{" +
                "_id=" + _id +
                ", titulo='" + titulo + '\'' +
                ", director='" + director + '\'' +
                ", precio=" + precio +
                '}';
    }
}
