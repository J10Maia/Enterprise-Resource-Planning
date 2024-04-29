import socket
import xml.etree.ElementTree as ET
import psycopg2
from psycopg2.extensions import ISOLATION_LEVEL_AUTOCOMMIT
import tkinter as tk
from tkinter import scrolledtext
from tkinter import messagebox
import threading
from datetime import datetime, timedelta

def parse_xml(xml_string):
    root = ET.fromstring(xml_string)
    client = root.find('Client').attrib['NameId']
    today = datetime.now()
    orders = [
        {
            'Number': order.attrib['Number'],
            'WorkPiece': order.attrib['WorkPiece'],
            'Quantity': int(order.attrib['Quantity']),
            'DueDate': today + timedelta(days=int(order.attrib['DueDate'])),
            'LatePen': int(order.attrib['LatePen']),
            'EarlyPen': int(order.attrib['EarlyPen']),
            'ClientNameId': client
        } for order in root.findall('Order')
    ]
    print(f"Parsed {len(orders)} orders for client {client}")
    return client, orders

def connect_db():
    hostname = 'db.fe.up.pt'  # Change to your database server IP or hostname
    database = 'infind202404'
    username = 'infind202404'
    password = 'RJiaKIGUvR'
    port = '5432'
    try:
        conn = psycopg2.connect(
            host=hostname,
            dbname=database,
            user=username,
            password=password,
            port=port
        )
        conn.set_isolation_level(ISOLATION_LEVEL_AUTOCOMMIT)
        print("Database connection established")
        return conn
    except psycopg2.DatabaseError as e:
        print(f"Database connection failed due to {e}")
        return None

def create_schema_and_tables(conn):
    schema_name = 'infind202404'
    commands = [
        f"CREATE SCHEMA IF NOT EXISTS {schema_name}",
        f"SET search_path TO {schema_name}",
        """
        CREATE TABLE IF NOT EXISTS Clients (
            NameId VARCHAR(255) PRIMARY KEY
        )
        """,
        """
        CREATE TABLE IF NOT EXISTS Orders (
            Number VARCHAR(255) PRIMARY KEY,
            WorkPiece VARCHAR(255),
            Quantity INTEGER,
            DueDate DATE,
            LatePen INTEGER,
            EarlyPen INTEGER,
            ClientNameId VARCHAR(255) REFERENCES Clients(NameId)
        )
        """
    ]
    cur = conn.cursor()
    try:
        for command in commands:
            cur.execute(command)
        print("Schema and tables created or verified successfully")
    except psycopg2.DatabaseError as e:
        print(f"Error in creating schema or tables: {e}")
    finally:
        cur.close()

def insert_data(conn, client, orders):
    cur = conn.cursor()
    try:
        # Insert or ignore existing client
        cur.execute("INSERT INTO Clients (NameId) VALUES (%s) ON CONFLICT (NameId) DO NOTHING", (client,))
        print(f"Client {client} inserted or already exists.")
        
        # Insert orders associated with the client
        for order in orders:
            cur.execute("INSERT INTO Orders (Number, WorkPiece, Quantity, DueDate, LatePen, EarlyPen, ClientNameId) VALUES (%s, %s, %s, %s, %s, %s, %s) ON CONFLICT (Number) DO NOTHING",
                        (order['Number'], order['WorkPiece'], order['Quantity'], order['DueDate'], order['LatePen'], order['EarlyPen'], client))
            print(f"Order {order['Number']} inserted or already exists.")
    except psycopg2.DatabaseError as e:
        print(f"Error inserting data: {e}")
    finally:
        cur.close()
        conn.commit()  # Commit changes

def udp_receiver(ip, port, log_widget):
    try:
        sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        sock.bind((ip, port))
        log_widget.insert(tk.END, f"Listening on {ip}:{port}\n")
        conn = connect_db()
        if conn:
            create_schema_and_tables(conn)

        while True:
            data, addr = sock.recvfrom(65535)
            xml_data = data.decode('utf-8')
            log_widget.insert(tk.END, f"Received data from {addr}\n")
            client, orders = parse_xml(xml_data)
            if client and orders:
                insert_data(conn, client, orders)
    except Exception as e:
        log_widget.insert(tk.END, f"An error occurred: {str(e)}\n")
    finally:
        sock.close()
        if conn:
            conn.close()

def start_receiver(ip, port, log_widget):
    log_widget.delete(1.0, tk.END)  # Clear log area
    if ip and port:
        threading.Thread(target=udp_receiver, args=(ip, int(port), log_widget), daemon=True).start()
    else:
        messagebox.showerror("Error", "IP and Port must be provided.")

app = tk.Tk()
app.title("UDP Receiver")

# Input fields for IP and Port with default values
ip_label = tk.Label(app, text="IP Address:")
ip_label.pack()
ip_entry = tk.Entry(app)
ip_entry.insert(0, "localhost")  # Default IP
ip_entry.pack()

port_label = tk.Label(app, text="Port:")
port_label.pack()
port_entry = tk.Entry(app)
port_entry.insert(0, "12345")  # Default Port
port_entry.pack()

# Log area
log_area = scrolledtext.ScrolledText(app, width=60, height=10)
log_area.pack()

# Start button
start_btn = tk.Button(app, text="Start Listening", command=lambda: start_receiver(ip_entry.get(), port_entry.get(), log_area))
start_btn.pack()

app.mainloop()
