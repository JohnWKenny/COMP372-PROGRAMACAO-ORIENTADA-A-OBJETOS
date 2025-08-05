import java.util.Scanner;

public class Main
{
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String name;
		double price;
		int quantity;

		System.out.println("Enter product data: ");
		System.out.print("Name: ");
		name = sc.nextLine();
		System.out.print("Price: ");
		price = sc.nextDouble();
		System.out.print("Quantity in stock: ");
		quantity = sc.nextInt();

		Product product = new Product(name, price, quantity);

		System.out.println(product.printProduct());
		System.out.print("Enter the number of products to be added in stock: ");
		quantity = sc.nextInt();
		System.out.println();
		product.addProducts(quantity);
		
		System.out.print("Enter the number of products to be removed from stock: ");
		quantity = sc.nextInt();
		System.out.println();
		product.removeProducts(quantity);
	}
}