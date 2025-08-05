public class Product
{
    //Diagrama UML
	private String name;
	private double price;
	private int quantity;

	public Product(String name, double price, int quantity)
	{
		this.setName(name);
		this.setPrice(price);
		this.setQuantity(quantity);
	}

	public double totalValueInStock() { return this.price * this.quantity; }

	public void addProducts(int quantity)
	{
	    if(quantity <= 0) { System.out.println("Quantidade invalida"); return; }
		
		this.quantity += quantity;

		System.out.println(this.printUdatedProduct());
	}

	public void removeProducts(int quantity)
	{
	    if(quantity <= 0) { System.out.println("Quantidade invalida."); return; }
		this.quantity -= quantity;

        System.out.println(this.printUdatedProduct());
	}
	
	// Getters e Setters
	public String getName() { return this.name; }
	public void setName(String name) { this.name = name; }
	
	public double getPrice() { return this.price; }
	public void setPrice(double price) 
	{ 
	    if(price <= 0) { System.out.println("Preço invalido."); return; }
	    
	    this.price = price; 
	}
	
    public int getQuantity() { return this.quantity; }
	public void setQuantity(int quantity) 
	{ 
	    if(quantity <= 0) { System.out.println("Quatidade invalida."); return; }
	    
	    this.quantity = quantity; 
	}
	
	// Métodos Adcionais
	public String printProduct()
	{
	    return "Product data: " + this.name + ", " + this.quantity +
		                   " units, Total: $ " + this.totalValueInStock() + "\n";
	}
	
	private String printUdatedProduct()
	{
	    return "Updated data: " + this.name + ", " + this.quantity +
		                   " units, Total: $ " + this.totalValueInStock() + "\n";
	}
}