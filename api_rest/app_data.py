from flask import Flask, jsonify, json, request, make_response
from jose import jwt
from werkzeug.security import generate_password_hash, check_password_hash
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
            return jsonify({'message' : 'Token is missing!'}), 401
        try: 
            data = jwt.decode(token, app.config['SECRET_KEY'])
        except:
            return jsonify({'message' : 'Token is invalid!'}), 401
        return f(*args, **kwargs)
    return decorated

############################################# USUARIO #############################################

@app.route("/user/create/", methods=['POST'])
def user_create():
    data = (request.get_json(force=True))
    con = cx_Oracle.connect(bd)
    cur = con.cursor()
    hashed_password = generate_password_hash(data['CONTRASENA'], method='sha256')
    sql = 'INSERT INTO USUARIO (USUARIO, CONTRASENA, NOM_AP, CORREO, FECHA_NAC, GENERO, ROL, ID_PAIS) VALUES (:1, :2, :3, :4, :5, :6, :7, :8)'
    cur.execute(sql, (data['USUARIO'], hashed_password, data['NOM_AP'], data['CORREO'], data['FECHA_NAC'], data['GENERO'], '1', data['ID_PAIS']))
    con.commit()
    cur.close()
    con.close()
    return jsonify({'message': 1})

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

@app.route('/user/login/', methods=['POST'])
def user_login():
    data = (request.get_json(force=True))
    
    con = cx_Oracle.connect(bd)
    cur = con.cursor()
    cur.execute("SELECT CONTRASENA FROM USUARIO WHERE USUARIO = '" + data['USUARIO'] + "'")
    row_headers=[x[0] for x in cur.description]
    rv = cur.fetchall()
    json_data=[]
    for line in rv:
        json_data.append(dict(zip(row_headers,line)))
    cur.close()
    con.close()
    
    try:
        json_data[0]['CONTRASENA']
    except:
        return jsonify({'message': 105})
    
    if check_password_hash(json_data[0]['CONTRASENA'], data['CONTRASENA']):
        token = jwt.encode({'usuario' : data['USUARIO'], 'exp' : datetime.datetime.utcnow() + datetime.timedelta(minutes=10)}, app.config['SECRET_KEY'])
        
        con = cx_Oracle.connect(bd)
        cur = con.cursor()
        cur.execute("UPDATE USUARIO SET TOKEN = '" + token + "' WHERE USUARIO = '" + data['USUARIO'] + "'")
        con.commit()
        cur.close()
        con.close()
        return jsonify({'TOKEN': token})
    return jsonify({'message': 106})

###################################################################################################

if __name__ == "__main__":
    app.run(debug=True, host='10.147.19.237', port=8000)