import React, { Fragment } from "react";
import "./App.css";

//components
import InputTodo from "./components/InputTodo";
import ListTodos from "./components/ListTodos";
import DeleteButton from "./components/DeleteButton";

function App() {
  return (
    <Fragment>
      <div className="container mx-auto text-center">
        <InputTodo />
        <div className="container1 mt-4 flex gap-12">
          <div className="element flex-1 text-center">
            <h2 className="text-xl font-semibold mb-2">To-Do</h2>
            <ListTodos table="todo" />
          </div>
          <div className="element flex-1 text-center">
            <h2 className="text-xl font-semibold mb-2">In Progress</h2>
            <ListTodos table="inprogress" />
          </div>
          <div className="element flex-1 text-center">
            <h2 className="text-xl font-semibold mb-2">Done</h2>
            <ListTodos table="finished" />
          </div>
          <DeleteButton />
        </div>
      </div>
    </Fragment>
  );
}

export default App;
