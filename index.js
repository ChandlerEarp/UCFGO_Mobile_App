var mongodb = require('mongodb');
var ObjectID = mongodb.ObjectID;
var crypto = require('crypto');
var express = require('express');
var bodyParser = require('body-parser');
const cookieParser = require('cookie-parser');

require('dotenv').config();

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

        app.post('/garage', (request,response,next)=>{
             var post_data = request.body;

             var garageName = post_data.garageLetter;
             var db = client.db('ucf_go');

             if(garageName == null){
                response.json('Error grabbing garage name');
                console.log('Error grabbing garage name');
                return;
             }


            var query = { garage: garageName}
            db.collection("car_locations").find(query).toArray(function(err, result){
                if(err) throw err;
                response.json(result);
                console.log(result);
            });
        });

        app.post('/garageResults', (request,response,next)=>{
             var post_data = request.body;

             var garageName = post_data.garageLetter;
             var db = client.db('ucf_go');

             if(garageName == null){
                response.json('Error grabbing garage name');
                console.log('Error grabbing garage name');
                return;
             }


            var query = { occupied: true, garage : garageName}
            db.collection("car_locations").find(query).toArray(function(err, result){
                if(err) throw err;
                response.json(result);
                console.log(result);
            });
        });

        app.post('/grabID', (request,response,next) =>{
            var post_data = request.body;

            var email = post_data.email;

            var db = client.db('ucf_go');
            db.collection('users').find({'email':email}).count(function(err,number){
                if(number == 0){
                    response.json('Email does not exist');
                    console.log('Email does not exist');
                    return null;
                }
                else{
                    db.collection('users').findOne({'email':email},function(err,user){
                        var userID = user._id;
                        if(userID == null){
                            response.json('Error in retrieving userID');
                            console.log('Error in retrieving userID');
                            return null;
                        }
                        else{
                            response.json(userID);
                            console.log(userID);
                            return userID;
                        }

                    })
                }
            })
        });

        app.post('/grabName', (request,response,next)=>{
            var post_data = request.body;
            var email = post_data.email;

            var db = client.db('ucf_go');db.collection('users')
                                                         .find({'email':email}).count(function(err,number){
                                                             if(number == 0){
                                                                 response.json('Email does not exist');
                                                                 console.log('Email does not exist');
                                                                 return null;
                                                             }
                                                             else{
                                                                 //Insert Data
                                                                 db.collection('users')
                                                                     .findOne({'email':email},function(err,user){
                                                                         var name = user.name;
                                                                         if(name == null){
                                                                             response.json('Error in retrieving name');
                                                                             console.log('Error in retrieving name');
                                                                             return null;
                                                                         }
                                                                         else{
                                                                             response.json(name);
                                                                             console.log(name);
                                                                             return name;
                                                                         }
                                                                     })
                                                             }
                                                         })
            });

        //const PORT = proces.env.PORT || 8080;
        let port = process.env.PORT;
        if(port == null || port == ""){
            port = 8000;
        }
        app.listen(port, ()=>{
            console.log('Connected to MongoDB Server, Running on port ' +  port);
        })
    }
});