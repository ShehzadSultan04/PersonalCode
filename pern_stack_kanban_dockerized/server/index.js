require('dotenv').config();

const express = require("express");
const app = express();
const cors = require("cors");
const pool = require("./db")

//middleware
app.use(cors());
app.use(express.json())


//routes//

// Helper function to validate table names
const validTableName = (name) => {
    const validNames = ['todo', 'inprogress', 'finished']; // Add your valid table names here
    return validNames.includes(name);
};

//create a todo

app.post("/kanban/:tableName", async (req, res) => {
    try {
        const {tableName} = req.params;
        if (!validTableName(tableName)) {
            return res.status(400).json({ error: "Invalid table name" });
        }
        const { description } = req.body;
        query = {
            text: `INSERT INTO ${tableName}(description) VALUES($1) RETURNING *`,
            values: [description]
        }
        const newTodo = await pool.query(query);
        res.json(newTodo.rows[0]);
    } catch (err) {
        console.error(err.message)
    }
})

//get all todo

app.get("/kanban/:tableName", async (req, res) => {
    const {tableName} = req.params;
    if (!validTableName(tableName)) {
        return res.status(400).json({ error: "Invalid table name" });
    }
    try {
        const query = {
            text: `SELECT * FROM ${tableName}`
        }
        const allTodos = await pool.query(query);
        res.json(allTodos.rows);
    } catch (err) {
        console.error(err.message);
    }
})

//get a todo

app.get("/kanban/:tableName/:id", async (req, res) => {
    try {
        const { tableName, id } = req.params;
        if (!validTableName(tableName)) {
            return res.status(400).json({ error: "Invalid table name" });
        }
        const query = {
            text: `SELECT * FROM ${tableName} WHERE todo_id = $1`,
            values: [id]
        }
        const todo = await pool.query(query);

        res.json(todo.rows[0])
    } catch (err) {
        console.error(err.message);
    }
})

// update a todo

app.put("/kanban/:tableName/:id", async (req, res) => {
    try {
        const { tableName, id } = req.params;
        if (!validTableName(tableName)) {
            return res.status(400).json({ error: "Invalid table name" });
        }

        const { description } = req.body;
        const query = {
            text: `UPDATE ${tableName} SET description = $1 WHERE todo_id = $2 RETURNING *`,
            values: [description, id]
        };

        const updateTodo = await pool.query(query);

        res.json(updateTodo.rows[0]);

    } catch (err) {
        console.error(err.message);
        res.status(500).json({ error: "Server error" });
    }
});


//delete a todo

app.delete("/kanban/:tableName/:id", async (req, res) => {
    try {
        const { tableName, id } = req.params;
        if (!validTableName(tableName)) {
            return res.status(400).json({ error: "Invalid table name" });
        }
        const query = {
            text:`DELETE FROM ${tableName} WHERE todo_id = $1`,
            values: [id]
        }
        const deleteTodo = await pool.query(query);

        res.json("Todo was deleted!");
    } catch (err) {
        console.error(err.message);
    }
})

const PORT = 8000;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
});