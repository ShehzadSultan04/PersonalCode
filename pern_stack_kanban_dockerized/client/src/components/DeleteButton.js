import React, { Fragment, useEffect, useState } from "react";
import { FiPlus, FiTrash } from "react-icons/fi";
// import { motion } from "framer-motion";
import { FaFire } from "react-icons/fa";

import ListTodos from "./ListTodos";

const DeleteButton = ({}) => {
  // Function to delete a todo
  const deleteTodo = async (id, table) => {
    try {
      await fetch(`http://localhost:8000/kanban/${table}/${id}`, {
        method: "DELETE",
      });
    } catch (err) {
      console.error(err.message);
    }
  };
  const [active, setActive] = useState(false);

  const handleDragOver = (e) => {
    e.preventDefault(); 
    setActive(true)
  }

  const handleDragLeave = () => {
    setActive(false);
  }
  
  const handleDragEnd = (e) => {
    const cardID = e.dataTransfer.getData("cardId");
    const table = e.dataTransfer.getData("table");

    deleteTodo(cardID, table)

    setActive(false);
  }

  return (
    <div
    onDrop={handleDragEnd}
    onDragOver={handleDragOver}
    onDragLeave={handleDragLeave}
      className={`mt-10 grid h-56 w-56 shrink-0 place-content-center rounded border text-3xl ${
        active
          ? "border-red-800 bg-red-800/20 text-red-500"
          : "border-neutral-500 bg-neutral-500/20 text-neutral-500"
      }`}
    >
        {active ? <FaFire className="animate-bounce" /> : <FiTrash />}
    </div>
  );
};

export default DeleteButton;
