import React, { Fragment, useState } from "react";
const InputTodo = () => {
  const [description, setDescription] = useState(""); //description is init to empty string, created setDescription to update description, and description to read the current state
  const [selectedOption, setSelectedOption] = useState("todo");

  const onSubmitForm = async (e) => {
    e.preventDefault(); //Prevent the page from reloading upon submission
    try {
      const body = { description, table: selectedOption };
      const response = await fetch(
        `http://localhost:8000/kanban/${selectedOption}`,
        {
          method: "POST", //Post new data, defined by
          headers: { "Content-Type": "application/json" }, //content type is json
          body: JSON.stringify(body), //include the object description in the body
        }
      );

      window.location = "/"; //Reloads the page after request is successfully sent
    } catch (err) {
      console.error(err.message);
    }
  };
  return (
    <Fragment>
      <h1 className="text-center mt-5 text-2xl font-semibold">
        Pern Todo List
      </h1>
      <div className="flex justify-center mt-5">
        <form
          className="flex items-center space-x-4 mt-5"
          onSubmit={onSubmitForm}
        >
          <input
            type="text"
            className="w-80 p-2 border border-gray-300 rounded"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Enter task description"
          />
          <select
            className="w-80 p-2 border border-gray-300 rounded"
            onChange={(e) => setSelectedOption(e.target.value)}
          >
            <option value="todo">To-do</option>
            <option value="inprogress">In Progress</option>
            <option value="finished">Done</option>
          </select>
          <button
            type="submit"
            className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
          >
            Add
          </button>
        </form>
      </div>
    </Fragment>
  );
};

export default InputTodo;
