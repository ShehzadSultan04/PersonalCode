import React, { Fragment, useState, useEffect } from 'react';

const EditTodo = ({ todo, table, onUpdate }) => {
  const [description, setDescription] = useState(todo.description);

  useEffect(() => {
    setDescription(todo.description);
  }, [todo.description]);

  const updateDescription = async (e) => {
    e.preventDefault();
    try {
      const body = { description };
      const response = await fetch(`http://localhost:8000/kanban/${table}/${todo.todo_id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
      });

      if (response.ok) {
        const updatedTodo = await response.json();
        onUpdate(updatedTodo);
        closeModal();
      } else {
        console.error('Failed to update the todo');
      }
    } catch (err) {
      console.error(err.message);
    }
  };

  const closeModal = () => {
    const modal = document.getElementById(`table${table}id${todo.todo_id}`);
    if (modal) {
      modal.classList.remove('show');
      modal.style.display = 'none';
      document.body.classList.remove('modal-open');
      const modalBackdrop = document.querySelector('.modal-backdrop');
      if (modalBackdrop) {
        modalBackdrop.parentNode.removeChild(modalBackdrop);
      }
    }
  };

  return (
    <Fragment>
      <button
        type="button"
        className="bg-yellow-500 text-white rounded px-2 py-1"
        data-toggle="modal"
        data-target={`#table${table}id${todo.todo_id}`}
      >
        Edit
      </button>

      <div
        className="modal fade"
        id={`table${table}id${todo.todo_id}`}
        tabIndex="-1"
        role="dialog"
        aria-labelledby="exampleModalLabel"
        aria-hidden="true"
        onClick={() => setDescription(todo.description)}
      >
        <div className="modal-dialog" role="document">
          <div className="modal-content">
            <div className="modal-header">
              <h5 className="modal-title" id="exampleModalLabel">Edit Todo</h5>
              <button
                type="button"
                className="close"
                data-dismiss="modal"
                aria-label="Close"
                onClick={() => {
                  setDescription(todo.description);
                  closeModal();
                }}
              >
                <span aria-hidden="true">&times;</span>
              </button>
            </div>
            <div className="modal-body">
              <input
                type="text"
                className="form-control"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
              />
            </div>
            <div className="modal-footer">
              <button
                type="button"
                className="bg-yellow-500 text-white rounded px-2 py-1"
                onClick={(e) => updateDescription(e)}
              >
                Confirm
              </button>
              <button
                type="button"
                className="bg-red-500 text-white rounded px-2 py-1"
                data-dismiss="modal"
                onClick={() => {
                  setDescription(todo.description);
                  closeModal();
                }}
              >
                Close
              </button>
            </div>
          </div>
        </div>
      </div>
    </Fragment>
  );
};

export default EditTodo;
