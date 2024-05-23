import socket
import os
import tkinter as tk
from tkinter import filedialog
from tkinter import scrolledtext
from tkinter import messagebox

def udp_sender(ip, port, directory, log_widget):
    try:
        sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        for filename in os.listdir(directory):
            if filename.endswith('.xml'):
                filepath = os.path.join(directory, filename)
                with open(filepath, 'rb') as file:
                    data = file.read()
                sock.sendto(data, (ip, port))
                log_widget.insert(tk.END, f"Data sent for {filename} to {ip}:{port}\n")
    except Exception as e:
        log_widget.insert(tk.END, f"An error occurred: {str(e)}\n")
    finally:
        sock.close()

def send_files(ip, port, log_widget):
    directory = filedialog.askdirectory()
    if directory and ip and port:
        log_widget.delete(1.0, tk.END)  # Clear log area
        udp_sender(ip, port, directory, log_widget)
    else:
        messagebox.showerror("Error", "All fields and directory must be selected.")

app = tk.Tk()
app.title("UDP Sender")

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

# Send button
send_btn = tk.Button(app, text="Send Files", command=lambda: send_files(ip_entry.get(), int(port_entry.get()), log_area))
send_btn.pack()

app.mainloop()
