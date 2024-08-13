import React, { Fragment, useEffect, useState } from "react";
import EditTodo from "./EditTodo";

const ListTodos = ({ table }) => {
  const [todos, setTodos] = useState([]);

  const deleteTodo = (id) => {
      setTodos(todos.filter((todo) => todo.todo_id !== id));
  };

  const getTodos = async () => {
    try {
      const response = await fetch(`http://localhost:8000/kanban/${table}`);
      const jsonData = await response.json();
      setTodos(jsonData);
    } catch (err) {
      console.error(err.message);
    }
  };

  // Function to update a todo in the state
  const handleUpdateTodo = (updatedTodo) => {
    const updatedTodos = todos.map((todo) =>
      todo.todo_id === updatedTodo.todo_id ? updatedTodo : todo
    );
    setTodos(updatedTodos);
  };

  useEffect(() => {
    getTodos();
  }, []);

  const handleDragStart = (e, card) => {
    e.dataTransfer.setData("cardID", card.todo_id);
    e.dataTransfer.setData("table", table);
  };

  const handleDragEnd = (e, card) => {
    deleteTodo(card.todo_id);
  }

  const Card = ({ todo, handleDragStart }) => {
    return (
      <div
        onDragStart={(e) => handleDragStart(e, todo)}
        onDragEnd={(e) => handleDragEnd(e, todo)}
        draggable="true"
        className="cursor-grab active:cursor-grabbing rounded border border-neutral-700 bg-white p-3 mb-2 mx-auto w-full max-w-md"
      >
        <div className="flex justify-between items-center text-black">
          <span className="text-sm flex-grow">{todo.description}</span>
          <EditTodo todo={todo} table={table} onUpdate={handleUpdateTodo} />
        </div>
      </div>
    );
  };

  return (
    <Fragment>
      <table className="table mt-2 text-center w-full">
        <tbody>
          {todos.map((todo) => (
            <Card
              handleDragStart={handleDragStart}
              key={todo.todo_id}
              todo={todo}
            ></Card>
          ))}
        </tbody>
      </table>
    </Fragment>
  );
};

export default ListTodos;
