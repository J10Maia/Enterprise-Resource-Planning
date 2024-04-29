import socket
import xml.etree.ElementTree as ET
import psycopg2
from psycopg2.extensions import ISOLATION_LEVEL_AUTOCOMMIT
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
    hostname = 'db.fe.up.pt'
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
        exit(1)

def create_schema_and_tables(conn):
    schema_name = 'INFI2024'
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
        cur.execute("INSERT INTO Clients (NameId) VALUES (%s) ON CONFLICT (NameId) DO NOTHING", (client,))
        print(f"Client {client} inserted or already exists")
        for order in orders:
            cur.execute("INSERT INTO Orders (Number, WorkPiece, Quantity, DueDate, LatePen, EarlyPen, ClientNameId) VALUES (%s, %s, %s, %s, %s, %s, %s) ON CONFLICT (Number) DO NOTHING",
                        (order['Number'], order['WorkPiece'], order['Quantity'], order['DueDate'], order['LatePen'], order['EarlyPen'], order['ClientNameId']))
            print(f"Order {order['Number']} inserted or already exists")
    except psycopg2.DatabaseError as e:
        print(f"Error inserting data: {e}")
    finally:
        cur.close()
    conn.commit()

def udp_receiver(ip, port):
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock.bind((ip, port))
    print(f"Listening on {ip}:{port}")

    conn = connect_db()
    create_schema_and_tables(conn)

    try:
        while True:
            data, addr = sock.recvfrom(65535)
            xml_data = data.decode('utf-8')
            print(f"Received data from {addr}")

            # Extract the XML from the received data
            xml_start_pos = xml_data.find('<?xml')
            if xml_start_pos != -1:
                xml_data = xml_data[xml_start_pos:]
                try:
                    client, orders = parse_xml(xml_data)
                    if client:
                        insert_data(conn, client, orders)
                except ET.ParseError as e:
                    print(f"Error parsing XML data: {e}")
            else:
                print("No valid XML data found in the received packet.")
    finally:
        conn.close()
        sock.close()

if __name__ == "__main__":
    udp_receiver('localhost', 12345)
