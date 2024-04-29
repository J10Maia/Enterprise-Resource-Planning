import socket
import os

def udp_sender(ip, port, directory):
    # Create a socket
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    # List all XML files in the given directory
    for filename in os.listdir(directory):
        if filename.endswith('.xml'):
            filepath = os.path.join(directory, filename)
            
            # Read the XML file data
            with open(filepath, 'rb') as file:
                data = file.read()

            # Include the filename at the start of the message, followed by a null byte
            message = filename.encode('utf-8') + b'\0' + data

            # Send the data to the specified IP and port
            sock.sendto(message, (ip, port))
            print(f"Data sent for {filename} to {ip}:{port}")

    # Close the socket
    sock.close()

# Asking for the directory path
directory = input("Please enter the directory path containing the XML files: ")
udp_sender('localhost', 12345, directory)
