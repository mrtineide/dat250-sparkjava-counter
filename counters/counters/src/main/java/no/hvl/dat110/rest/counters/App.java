package no.hvl.dat110.rest.counters;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App {
	
	static Counters counters = null;
	static List<Todo> todos = null;
	
	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		counters = new Counters();
		todos = new ArrayList<>();

		
		after((req, res) -> {
  		  res.type("application/json");
  		});
		
		get("/hello", (req, res) -> "Hello World!");
		
        get("/counters", (req, res) -> counters.toJson());
 
        get("/counters/red", (req, res) -> counters.getRed());

        get("/counters/green", (req, res) -> counters.getGreen());

        // put for green/red and in JSON
        // variant that returns link/references to red and green counter
        put("/counters", (req,res) -> {
        
        	Gson gson = new Gson();
        	
        	counters = gson.fromJson(req.body(), Counters.class);
        
            return counters.toJson();
        	
        });

		get("/todo",(request,response)->{

			Gson gson = new Gson();

			String jsonString = gson.toJson(todos);

			return jsonString;
		});

		get("/todo/*",(request,response)->{

			Gson gson = new Gson();

			int index = Integer.parseInt(request.splat()[0])-1;

			Todo todo = todos.get(index);

			String jsonString = gson.toJson(todo);

			return jsonString;
		});

		post("/todo",(request,response)->{

			Gson gson = new Gson();

			Todo todo = gson.fromJson(request.body(),Todo.class);


			todos.add(todo);

			return gson.toJson(todo);
		});

		put("/todo/*",(request,response)->{

			Gson gson = new Gson();

			int index = Integer.parseInt(request.splat()[0])-1;

			Todo todo = todos.get(index);

			Todo new_todo = gson.fromJson(request.body(), Todo.class);

			todo.setDescription(new_todo.getDescription());
			todo.setSummary(new_todo.getSummary());

			return gson.toJson(todo);
		});

		delete("/todo/*",(request,response)->{

			Gson gson = new Gson();

			String[] splats = request.splat();

			Integer n = Integer.parseInt(splats[0]);

			todos.remove(n-1);


			return gson.toJson(todos);
		});


    }

}
