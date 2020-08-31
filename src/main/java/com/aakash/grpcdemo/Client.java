package com.aakash.grpcdemo;

import com.aakash.grpcdemo.generated.Todo.TodoItem;
import com.aakash.grpcdemo.generated.TodoServiceGrpc;
import com.aakash.grpcdemo.generated.TodoServiceGrpc.TodoServiceBlockingStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Client {
	
	public static void main(String[] args) {
		 ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
		          .usePlaintext()
		          .build();
		 
		        TodoServiceBlockingStub stub 
		          = TodoServiceGrpc.newBlockingStub(channel);

		        TodoItem item = TodoItem.newBuilder().setTodo("commit to git").build();
		        
		        System.out.println("[client] : asking server to add todo {" + item.getTodo() + "}");
		        
		        TodoItem response = stub.createTodo(item);
		 
		        System.out.println("[client] : received response from server {" + response.getTodo() + "}");
		        
		        channel.shutdown();
	}

}
