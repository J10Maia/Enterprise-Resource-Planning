const express = require('express');
const { Client } = require('pg');
const cors = require('cors');
const app = express();
const port = 3000;

// Database connection configuration
const connectionString = 'postgresql://infind202404:RJiaKIGUvR@db.fe.up.pt/infind202404';

const client = new Client({
    connectionString: connectionString
});

client.connect(err => {
    if (err) {
        console.error('Connection error', err.stack);
    } else {
        console.log('Connected to the database');
    }
});

// Enable CORS
app.use(cors());

app.use(express.static('public'));

app.get('/api/orders', async (req, res) => {
    try {
        const result = await client.query("SELECT number, workpiece, quantity, duedate, latepenalty, earlypenalty, clientnameid FROM infi2024.orders");
        res.json(result.rows);
    } catch (err) {
        console.error(err);
        res.status(500).send('Error fetching orders');
    }
});

app.get('/api/planningtable', async (req, res) => {
    try {
        const result = await client.query("SELECT id, day, peca_inicial, peca_final FROM infi2024.planningtable");
        res.json(result.rows);
    } catch (err) {
        console.error(err);
        res.status(500).send('Error fetching planning data');
    }
});

app.get('/api/duration', async (req, res) => {
    try {
        const result = await client.query("SELECT * FROM infi2024.duration");
        res.json(result.rows);
    } catch (err) {
        console.error(err);
        res.status(500).send('Error fetching planning data');
    }
});

app.get('/api/current_minute', async (req, res) => {
    try {
        const result = await client.query("SELECT * FROM infi2024.current_minute ORDER BY minute DESC LIMIT 1");
        res.json(result.rows);
    } catch (err) {
        console.error(err);
        res.status(500).send('Error fetching planning data');
    }
});

app.get('/api/refused_order', async (req, res) => {
    try {
        const result = await client.query("SELECT * FROM infi2024.refused_order");
        res.json(result.rows);
    } catch (err) {
        console.error(err);
        res.status(500).send('Error fetching planning data');
    }
});

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}/`);
});
