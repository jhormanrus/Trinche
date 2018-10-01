from flask import Flask, jsonify, json, request, make_response
from jose import jwt
import datetime
from functools import wraps
import cx_Oracle

app = Flask(__name__)
bd = 'trinche/123@localhost:1521/xe'

app.config['SECRET_KEY'] = 'lallaveestaentucorazon'

def token_required(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        token = request.args.get('token')
        if not token:
            return jsonify({'message' : 'Token is missing!'}), 403
        try: 
            data = jwt.decode(token, app.config['SECRET_KEY'])
        except:
            return jsonify({'message' : 'Token is invalid!'}), 403
        return f(*args, **kwargs)
    return decorated

@app.route("/readall/", methods=['GET'])
@token_required
def readall():
    con = cx_Oracle.connect(bd)
    cur = con.cursor()
    cur.execute('SELECT * FROM USUARIO')
    row_headers=[x[0] for x in cur.description]
    rv = cur.fetchall()
    json_data=[]
    for line in rv:
        json_data.append(dict(zip(row_headers,line)))
        #s = json.dumps(json_data)
    cur.close()
    con.close()
    return jsonify(json_data)

@app.route("/read/", methods=['POST'])
@token_required
def read():
    data = (request.get_json(force=True))
    con = cx_Oracle.connect(bd)
    cur = con.cursor()
    print (data)
    cur.execute("SELECT * FROM USUARIO WHERE ID_USUARIO = '"+data['id']+"'")
    row_headers=[x[0] for x in cur.description]
    rv = cur.fetchall()
    json_data=[]
    for line in rv:
        json_data.append(dict(zip(row_headers,line)))
        #s = json.dumps(json_data)
    cur.close()
    con.close()
    return jsonify(json_data)

@app.route('/login/', methods=['POST'])
def login():
    data = (request.get_json(force=True))
    print(data['username'] + data['password'])
    #hashed_password = generate_password_hash(data['password'], method='sha256')
    if data['username'] == 'jhorman' and data['password'] == 'tito':
        token = jwt.encode({'username' : data['username'], 'exp' : datetime.datetime.utcnow() + datetime.timedelta(minutes=10)}, app.config['SECRET_KEY'])
        return jsonify({'message': 'Bienvenido ' + data['username'] , 'token': token})
    return make_response('Could not verify!', 401, {'WWW-Authenticate' : 'Basic realm="Login Required"'})

if __name__ == "__main__":
    app.run(debug=True, host='0.0.0.0', port=8000)