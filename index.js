var mongodb = require('mongodb');
var ObjectID = mongodb.ObjectID;
var crypto = require('crypto');
var express = require('express');
var bodyParser = require('body-parser');

var genRandomString = function(length){
    return crypto.randomBytes(Math.ceil(length/2))
        .toString('hex')
        .slice(0,length);
};

var sha512 = function(password,salt){
    var hash = crypto.createHmac('sha512',salt);
    hash.update(password.toString());
    var value = hash.digest('hex');
    return{
        salt:salt,
        passwordHash:value
    };
};

function saltHashPassword(userPassword){
    var salt = genRandomString(16);
    var passwordData = sha512(userPassword,salt);
    return passwordData;
}

function checkHashPassword(userPassword,salt){
    var passwordData = sha512(userPassword,salt);
    return passwordData;
}

//Create Express Service
var app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended:true}));

//Create mongoDB Client
var MongoClient = mongodb.MongoClient;

//Connection URL
//var url = 'mongodb://127.0.0.1:27017';
var uri = "mongodb+srv://dbUser:userPassword@cluster0.wxlns.mongodb.net/ucf_go?retryWrites=true&w=majority";

MongoClient.connect(uri, {useNewUrlParser: true,  useUnifiedTopology: true },function(err,client){
    if(err)
        console.log('Unable to connect to the mongoDB server,Error',err);
    else{

        app.post('/register', (request,response,next) =>{
            var post_data = request.body;

            var plaint_password = post_data.password;
            var hash_data = saltHashPassword(plaint_password);

            var password = hash_data.passwordHash; //Save password hash
            var salt = hash_data.salt; //Save salt

            var name = post_data.name;
            var email = post_data.email;

            var insertJson = {
                'email' : email,
                'password' : password,
                'salt' : salt,
                'name' : name
            };
            var db = client.db('ucf_go');

            //check email exists
            db.collection('users')
                .find({'email':email}).count(function(err,number){
                    if(number != 0){
                        response.json('Email already exists');
                        console.log('Email already exists');
                    }
                    else{
                        //Insert Data
                        db.collection('users')
                            .insertOne(insertJson,function(error,res){
                                response.json('Registration success');
                                console.log('Registration success');
                            })
                    }
                })
        });

        app.post('/login', (request,response,next) =>{
            var post_data = request.body;

            var email = post_data.email;
            var userPassword = post_data.password;
            
            var db = client.db('ucf_go');

            //check email exists
            db.collection('users')
                .find({'email':email}).count(function(err,number){
                    if(number == 0){
                        response.json('Email does not exist');
                        console.log('Email does not exist');
                    }
                    else{
                        //Insert Data
                        db.collection('users')
                            .findOne({'email':email},function(err,user){
                                var salt = user.salt; //Get salt from user
                                var hashed_password = checkHashPassword(userPassword,salt).passwordHash;
                                var encrypted_password = user.password; //Get password from user
                                if(hashed_password == encrypted_password){
                                    response.json('Login success');
                                    console.log('Login success');
                                }
                                else{
                                    response.json('Wrong password');
                                    console.log('Wrong password');
                                }
                            })
                    }
                })
        });

        app.listen(3000, ()=>{
            console.log('Connected to MongoDB Server, Running on port 3000');
        })
    }
});