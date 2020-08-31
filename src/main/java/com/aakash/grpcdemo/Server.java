package com.aakash.grpcdemo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.aakash.grpcdemo.generated.Todo.TodoItem;
import com.aakash.grpcdemo.generated.Todo.TodoItems;
import com.aakash.grpcdemo.generated.TodoServiceGrpc.TodoServiceImplBase;

import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class Server {
	public static void main(String[] args) {
		new Server().start();
		
	}
	
	 private void start() {
		 System.out.println("[server] : starting grpc todo sevice server...");
		 io.grpc.Server server = ServerBuilder
		          .forPort(8080)
		          .addService(new TodoServiceImpl()).build();
		 
		        try {
		        	server.start();
					 System.out.println("[server] : started grpc todo sevice server, listning on port 8080");
		        	server.awaitTermination();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				 System.out.println("[server] : terminating grpc todo service server");
	}

	class TodoServiceImpl extends TodoServiceImplBase {
		
		private final List<TodoItem> items = new ArrayList<>();
		
		@Override
		public void createTodo(TodoItem item, StreamObserver<TodoItem> responseObserver) {
			 System.out.println("[server] : received request to add todo {" + item.getTodo() + "}");
			
			//add todo to db (inmemory list as of now)
			items.add(item);
			
			//Build response
			TodoItem response =  TodoItem.newBuilder().setTodo("added todo - " + item.getTodo()).build();

			System.out.println("[server] : sending response {" + response.getTodo() + "}");
		
			responseObserver.onNext(response);
			responseObserver.onCompleted();
			 System.out.println("[server] : sent reponse");

		}
		
		@Override
		public void getAllSync(com.aakash.grpcdemo.generated.Todo.Void request, StreamObserver<TodoItems> responseObserver) {
			System.out.println("[server] : get all sync requested");
	    	populateAll(responseObserver);
	    	System.out.println("[server] : get all sync returned");
	    }

		@Override
	    public void getAllAsync(com.aakash.grpcdemo.generated.Todo.Void request, StreamObserver<TodoItem> responseObserver) {
	    	System.out.println("[server] : get all async requested");
	    	populateAllAsync(responseObserver);
	    	System.out.println("[server] : get all async returned");
	    }

		private void populateAllAsync(StreamObserver<TodoItem> responseObserver) {
			items.forEach(responseObserver::onNext);
			responseObserver.onCompleted();
		}

		private void populateAll(StreamObserver<TodoItems> responseObserver) {
			TodoItems.Builder builder = TodoItems.newBuilder();
			items.forEach(builder::addItems);
			responseObserver.onNext(builder.build());
			responseObserver.onCompleted();
		}

		
	}
}
