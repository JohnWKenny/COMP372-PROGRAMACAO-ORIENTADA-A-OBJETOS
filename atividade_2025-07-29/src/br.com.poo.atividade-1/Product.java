public class Product
{
	private String name;
	private double price;
	private int quantity;

	public Product(String name, double price, int quantity)
	{
		if(price < 0 || quantity < 0)
		{
			System.out.println("Objeto nao contém valores válidos!!!");
			return;
		}
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		
		System.out.println("Product data: " + this.name + ", " + this.quantity +
		                   " units, Total: $ " + TotalValueInStock() + "\n");
	}

	public double TotalValueInStock()
	{
		return this.price * this.quantity;
	}

	public void AddProducts(int quantity)
	{
		this.quantity += quantity;

		System.out.println("Updated data: " + this.name + ", " + this.quantity +
		                   " units, Total: $ " + TotalValueInStock() + "\n");
	}

	public void RemoveProducts(int quantity)
	{
		this.quantity -= quantity;

        System.out.println("Updated data: " + this.name + ", " + this.quantity +
		                   " units, Total: $ " + TotalValueInStock() + "\n");
	}
}