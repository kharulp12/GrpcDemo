package com.aakash.grpcdemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Scanner;

import com.aakash.grpcdemo.generated.Todo.TodoItem;
import com.aakash.grpcdemo.generated.Todo.TodoItems;
import com.aakash.grpcdemo.generated.Todo.Void;
import com.aakash.grpcdemo.generated.TodoServiceGrpc;
import com.aakash.grpcdemo.generated.TodoServiceGrpc.TodoServiceBlockingStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Client {
	
	public static void main(String[] args) {
		System.out.println("[client] : Starting client console");
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
		          .usePlaintext()
		          .build();
		 
		        TodoServiceBlockingStub stub 
		          = TodoServiceGrpc.newBlockingStub(channel);
		printHelp();
		Scanner sc = new Scanner(System.in);
		do {
			String cmd = sc.nextLine().trim();
			try {
				switch(cmd) {
					case "q" :
					case "quite" :
						System.out.println("[client] : shutting down client...");
						channel.shutdown();
						sc.close();
						System.exit(0);
						break;
					case "ag" :
					case "async-get" :
						System.out.println("[client] : asking server to get all Todos in asynchronus manner");
			        	Iterator<TodoItem> items = stub.getAllAsync(Void.newBuilder().build());
			        	while(items.hasNext()) {
			        		System.out.println("[client] : received response from server [" + items.next().getTodo() + "]");
			        	}
			        	System.out.println("[client] : completed async getAll");
			        	break;
					case "g" :
					case "get" :
						System.out.println("[client] : asking server to get all Todos in synchronus manner");
			        	TodoItems syncItems = stub.getAllSync(Void.newBuilder().build());
			        	System.out.println("[client] : received response from server - [\n" + 
			        			syncItems.getItemsList().stream().map(TodoItem::getTodo).reduce((a, b) -> a + "\n" + b).orElse("") 
			        			+"\n]");
			        	break;
		        	default :
		        		String message = cmd.split(" ", 2)[1];
		        		TodoItem item = TodoItem.newBuilder().setTodo(message).build();
		        		System.out.println("[client] : asking server to add todo");
			        	TodoItem response = stub.createTodo(item); 
			        	System.out.println("[client] : server reacted to adding a todo [" + response.getTodo() + "]");
			        		
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
			//flush everything and give some time to print console
			flush();
		} while (true);
	}

	private static void printHelp() {
		System.out.println("commands :");
		System.out.println("add [a] <message> : add todo of content <message>");
		System.out.println("get [g] : get all todos from server in sync manner");
		System.out.println("async-get [ag] : get all todos in async manner");
		System.out.println("quite [q] : to quite the console");
		flush();
	}

	private static void flush() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.print(">>");
		System.out.flush();
	}

}
