package dev.timbuksov;
import jakarta.persistence.*;import java.math.BigDecimal;
@Entity public class Product { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;public String name;public String description;@Column(precision=12,scale=2) public BigDecimal price;public int stock;protected Product(){}public Product(String name,String description,BigDecimal price,int stock){this.name=name;this.description=description;this.price=price;this.stock=stock;} }
