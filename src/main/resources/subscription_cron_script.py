import mysql.connector
from mysql.connector import Error
import datetime, requests, json


class Colors:
	def __init__(self):
		pass

	HEADER = '\033[95m'
	BLUE = '\033[94m'
	GREEN = '\033[92m'
	WARNING = '\033[93m'
	FAIL = '\033[91m'
	END = '\033[0m'
	BOLD = '\033[1m'
	UNDERLINE = '\033[4m'


def print_error(message):
	print Colors.FAIL + message + Colors.END


def print_info(message):
	print Colors.BLUE + message + Colors.END


def print_success(message):
	print Colors.GREEN + message + Colors.END


def connect():
    """ Connect to MySQL database """
    try:
        conn = mysql.connector.connect(host='localhost',
                                       database='shopping',
                                       user='root',
                                       password='root')
        if conn.is_connected():
		print '###############################'
        	print '# Connected to MySQL database #'
		print '###############################\n'
 
    except Error as e:
        print(e)
 
    return conn

if __name__ == '__main__':
	connection = connect()
	cursor = connection.cursor()
	now = datetime.datetime.now()
	customer_subscriptions_map = {}
	cursor.execute("SELECT customer_id, subscription_id FROM subscription WHERE next_due_date < '"+'{}-{}-{}'.format(now.strftime('%Y'), now.strftime('%m'), int(now.strftime('%d')) + 1)+"'")
	for (customer_id, subscription_id,) in cursor:
		if not customer_id in customer_subscriptions_map:
			customer_subscriptions_map[customer_id] = []

		customer_subscriptions_map[customer_id].append(subscription_id)

	for customer_id in customer_subscriptions_map:
		subscription_ids = customer_subscriptions_map[customer_id]
		print '-------------------------------------------------------------------------'
		print_info('\tAPI call to create order(s) for subscription IDs: {}'.format(subscription_ids))
		try:
			body = {'subscription_ids': subscription_ids}
			headers = {'Content-type': 'application/json', 'Accept': 'application/json'}
			response = requests.post('http://localhost:8888/api/order/subscription/', data = json.dumps(body), headers = headers)
			if response.status_code == 201:
				print_success('\tResponse status code - {}'.format(response.status_code))
				order_ids = []
				for order in response.json()['orders']:
					order_ids.append(order['order_id'])
				print_info('\tOrder IDs of newly created orders: {}'.format(order_ids))
			else:
				print_error('\tResponse status code - {}'.format(response.status_code))
				print_error('\tError! Response - {}'.format(json.dumps(response.json())))
		except requests.exceptions.RequestException as e:
			print_error('\tFailed to create orders from subscriptions')
			print e
		print '-------------------------------------------------------------------------'
	if cursor.rowcount <= 0:
		print_info('\nNo subscriptions were due today!\n')
	elif cursor.rowcount == 1:
		print_info("\nProcessed {} subscription".format(cursor.rowcount))
	else:
		print_info("\nProcessed {} subscriptions".format(cursor.rowcount))
	cursor.close()
	connection.close()
	print '\n#######################################'
	print '# Connection to MySQL Database closed #'
	print '#######################################'
	
