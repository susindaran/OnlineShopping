import mysql.connector
from mysql.connector import Error
import datetime
import requests

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
	cursor.execute("SELECT subscription_id FROM subscription WHERE next_due_date >= '" + now.strftime("%Y-%m-%d")+"' AND next_due_date < '"+'{}-{}-{}'.format(now.strftime('%Y'), now.strftime('%m'), int(now.strftime('%d')) + 1)+"'")
	for (subscription_id,) in cursor:
		print '-------------------------------------------------------------------------'
		print '\tCalling create order from subscription API for subscription ID: {}'.format(subscription_id)
		try:
			response = requests.post('http://localhost:8888/api/order/subscription/{}'.format(subscription_id))
			print '\tOrder created with Order ID: {}'.format(response.json()['order_id'])
		except requests.exceptions.RequestException as e:
			print 'Failed to create subscription'
			print e
		print '-------------------------------------------------------------------------'
	if cursor.rowcount <= 0:
		print '\nNo subscriptions were due today!\n'
	elif cursor.rowcount == 1:
		print "Processed {} subscription".format(cursor.rowcount)
	else:
		print "Processed {} subscriptions".format(cursor.rowcount)
	cursor.close()
	connection.close()
	print '\n#######################################'
	print '# Connection to MySQL Database closed #'
	print '#######################################'
	
