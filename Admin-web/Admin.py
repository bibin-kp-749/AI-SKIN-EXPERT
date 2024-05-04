
from flask import *
from DBConnection import *

app = Flask(__name__)
app.secret_key="helkldkfdof"
from datetime import datetime


staticpath="C:\\Users\\91773\\PycharmProjects\\Admin\\static\\"

@app.route('/reg_d')
def reg_d():
    return render_template('Doctor/registerindex.html')

@app.route('/')
def login():
    return render_template('index.html')

@app.route('/login_post',methods=['post'])
def login_post():
    username=request.form['textfield']
    password=request.form['textfield2']
    db=Db()
    qry="select * from login where Username='"+username+"' and Password='"+password+"'"
    res=db.selectOne(qry)
    if res is not None:
        session["lid"]=res["Login_id"]
        if res['User_Type']=="admin":
            return redirect('/home')
        elif res['User_Type']=="doctor":
            return redirect('/dr_home')
        else:
            return '''<script>alert('User Not Found');window.location='/'</script>'''
    else:
        return '''<script>alert('User Not Found');window.location='/'</script>'''



@app.route('/home')
def home():
    return render_template('admin/adminindex.html')

@app.route('/view_doctor')
def view_doctor():
    qry="select * from doctor where Status='Pending' or 'Rejected' "
    db=Db()
    res=db.select(qry)
    return render_template('admin/approve_doctor.html',val=res)


@app.route('/approve/<id>')
def approve(id):
    db = Db()
    qry = "UPDATE `doctor` SET `Status`='Approved' WHERE `Login_id`='"+str(id)+"'"
    res = db.update(qry)
    qry1 = "UPDATE `login` SET `User_Type`='doctor' WHERE `Login_id`='"+str(id)+"'"
    res1 = db.update(qry1)
    return '''<script>alert('Approved');window.location='/view_doctor'</script>'''

@app.route('/reject/<id>')
def reject(id):
    db=Db()
    qry="update `doctor` set status='Rejected' WHERE Login_id='"+str(id)+"'"
    res=db.update(qry)
    return "ok"

@app.route('/approved')
def approved():
    qry="SELECT * FROM `doctor` WHERE `Status`='approved'"
    db=Db()
    res=db.select(qry)
    return render_template('admin/approved.html',val=res)

@app.route('/disease')
def disease():
    return render_template('admin/disease.html')

@app.route('/disease_post',methods=['post'])
def disease_post():
    print(request.files)
    dname=request.form['textfield']
    dimage=request.files['fileField']
    dimage.save("C:\\Users\\91773\\PycharmProjects\\Admin\\static\\disease\\"+dimage.filename)
    file="/static/disease/"+dimage.filename
    ddescription=request.form['textarea']

    dtype=request.form['select']
    qry="insert into disease values('','"+dname+"','"+file+"','"+ddescription+"','"+dtype+"')"
    db=Db()
    db.insert(qry)
    return "ok"

@app.route('/edisease/<id>')
def edisease(id):
    db=Db()
    qry="select * from disease where Disease_id='"+str(id)+"'"
    res=db.selectOne(qry)
    return render_template('admin/edit disease.html',val=res)

@app.route('/edisease_post',methods=['post'])
def edisease_post():
    idr=request.form['id']
    ediseasename=request.form['textfield']
    eimage=request.files['fileField']
    # eimage.save("C:\\Users\\91773\\PycharmProjects\\Admin\\static\\disease\\"+eimage.filename)
    # fname="/static/disease/"+eimage.filename
    date=datetime.now().strftime("%y%m%d-%H%M%S")
    eimage.save(r"C:\\Users\\91773\\PycharmProjects\\Admin\\static\\disease\\"+date+".jpg")
    path=("/static/disease/"+date+".jpg")
    edescription=request.form['textarea']
    etype=request.form['select']
    if request.files!=None:
        if eimage.filename!="":
            qry="update disease set Disease_Name='"+ediseasename+"',Image='"+str(path)+"',Description='"+edescription+"',Type='"+etype+"' where Disease_id='"+idr+"' "
            db=Db()
            res=db.update(qry)
        else:
            qry = "update disease set Disease_Name='" + ediseasename + "',Description='" + edescription + "',Type='" + etype + "' where Disease_id='" + idr + "' "
            db = Db()
            res = db.update(qry)

    else:
        qry = "update disease set Disease_Name='" + ediseasename + "',Description='" + edescription + "',',Type='" + etype + "' where Disease_id='" + idr + "' "
        db = Db()
        res = db.update(qry)
    return '''<script>alert('Update Successfully');window.location="/view_disease"</script>'''


@app.route('/view_disease')
def view_disease():
    qry="select * from disease"
    db=Db()
    res=db.select(qry)
    return render_template('admin/view disease.html',val=res)

@app.route('/delete_disease/<id>')
def delete_disease(id):
    db=Db()
    qry="delete from disease where Disease_id='"+id+"'"
    res=db.delete(qry)
    return '''<script>alert('Delete Successfully');window.location="/view_disease"</script>'''

@app.route('/symptom2')
def symptom2():
    qry="select * from disease "
    db=Db()
    res=db.select(qry)
    return render_template('admin/symptom2.html',val=res)

@app.route('/search_symptoms',methods=['post'])
def search_symptoms():
    src=request.form['search']
    db=Db()
    qry="select `symptoms`.*,`disease`.`Disease_Name` from `symptoms` join `disease` on `disease`.`Disease_id`=`symptoms`.`Disease_id` where Disease_Name like '%"+src+"%'"
    res=db.select(qry)
    return render_template('admin/view symptom.html',val=res)


@app.route('/prediction',methods=['post'])
def prediction():

    s=request.form["s"]





    db=Db()
    qry="select * from disease"
    res=db.select(qry)


    qry="SELECT DISTINCT `Symptoms` FROM symptoms"
    res1=db.select(qry)

    symptoms=[]
    for i in res1:
        symptoms.append(i['Symptoms'])


    testt=[]
    se= s.split(",")


    for i in symptoms:
        if i in se:
            testt.append(1)
        else:
            testt.append(0)

    diseasename=[]

    diseasefeatures=[]
    diseaseid=[]
    for i in res:
        diseasename.append(i['Disease_Name'])
        diseaseid.append(i['Disease_id'])



        qry2="SELECT `Symptoms` FROM symptoms WHERE Disease_id='"+str(i['Disease_id'])+"'"
        res3=db.select(qry2)

        temp=[]
        for j in res3:
            temp.append(j['Symptoms'])


        s=[]

        for m in symptoms:
            if m in temp:
                s.append(1)
            else:
                s.append(0)

        diseasefeatures.append(s)

    print(diseasename)
    print(diseasefeatures)


    from sklearn.ensemble import  RandomForestClassifier

    rnd=RandomForestClassifier()
    rnd.fit(diseasefeatures,diseaseid)


    p= rnd.predict([testt])


    print("result",p)

    qry2 = "SELECT * FROM `disease` WHERE `Disease_id`='" + str(p[0]) + "'"
    res2 = db.selectOne(qry2)
    if res is not None:

        return jsonify(status='ok', name=res2["Disease_Name"], des=res2["Description"], type=res2["Type"],
                       image=res2["Image"])
    else:
        return jsonify(status="no")


# @app.route('/predict_disease')
# def predict_disease():
#





@app.route('/viewsymptom2/<id>')
def viewsymptom2(id):
    session['did']=id
    qry="SELECT  * FROM `symptoms` WHERE `Disease_id`='"+id+"'"
    db=Db()
    res=db.select(qry)

    return render_template("admin/view symptom2.html",val=res)


@app.route('/symptom/<id>')
def symptom(id):
    qry="select * from disease where Disease_id = '"+id+"'"
    db=Db()
    res=db.selectOne(qry)
    return render_template('admin/symptom.html',val=res)

@app.route('/delete_symptom/<id>')
def delete_symptom(id):
    db=Db()
    qry="delete from symptoms where Symptom_id='"+id+"'"
    res=db.delete(qry)
    return '''<script>alert('Delete Successfully');window.location="/view_disease"</script>'''

@app.route('/symptoms_post',methods=['POST'])
def symptoms_post():
    idr=request.form['id']
    ssymptomdisease=request.form['textarea']
    qry="insert into symptoms values('','"+idr+"','"+ssymptomdisease+"')"
    db=Db()
    res=db.insert(qry)
    return redirect('/view_disease')

@app.route('/symptoms_post2',methods=['POST'])
def symptoms_post2():
    idr=request.form['select']
    ssymptomdisease=request.form['textarea']
    qry="insert into symptoms values('','"+idr+"','"+ssymptomdisease+"')"
    db=Db()
    res=db.insert(qry)
    return redirect('/symptom2')



@app.route('/view_symptom')
def  view_symptom():
    qry="select `symptoms`.*,`disease`.`Disease_Name` from `symptoms` join `disease` on `disease`.`Disease_id`=`symptoms`.`Disease_id`"
    db=Db()
    res=db.select(qry)
    return render_template('admin/view symptom.html',val=res)


@app.route('/edit_symptom/<id>')
def edit_symptom(id):
    session['symid']=id
    db=Db()
    qry="select *  from symptoms where Symptom_id='"+str(id)+"'"
    res=db.selectOne(qry)
    return render_template('Admin/edit symptom.html',val=res)


@app.route('/edit_symptompost',methods=['post'])
def edit_symptompost():
    did=str(session['did'])
    esymptom=request.form['textarea']
    qry="update symptoms set Symptoms='"+esymptom+"' where Symptom_id='"+str(session['symid'])+"' "
    db=Db()
    res=db.update(qry)
    return "<script>alert('Update Successfully');window.location="/viewsymptom2/+str(did)/"</script>"



@app.route('/user')
def user():
    qry="select * from user"
    db=Db()
    res=db.select(qry)
    return render_template('admin/view user.html',val=res)

@app.route('/feedback')
def feedback():
    qry="SELECT * from feedback,user where feedback.User_id=user.Login_id "
    db=Db()
    res=db.select(qry)
    return render_template('admin/view feedback.html',val=res)

@app.route('/review')
def review():
    qry="select * from reviews,user where reviews.User_id=user.User_id"
    db=Db()
    res=db.select(qry)
    return render_template('admin/view doctor review.html',val=res)

###################################################################################################################################

@app.route('/dr_home')
def dr_home():
    return render_template('Doctor/docindex.html')

@app.route('/doctor_signup')
def doctor_signup():
    return render_template('Doctor/regindex.html')

@app.route('/signup_post',methods=['post'])
def signup_post():
    dName=request.form['textfield3']
    dEmail=request.form['textfield2']
    dPhone=request.form['textfield']
    dQualification=request.form['textfield4']
    dGender=request.form['RadioGroup1']
    dExperiance=request.form['textfield5']
    dPlace=request.form['textfield6']
    password=request.form['textfield7']
    confirmpassword=request.form['textfield8']
    photo=request.files["photo"]
    date = datetime.now().strftime("%y%m%d-%H%M%S")
    path="/static/doctor_img/"+str(date)+".jpg"
    photo.save("C:\\Users\\91773\\PycharmProjects\\Admin\\static\\doctor_img\\"+str(date)+".jpg")
    db=Db()
    if password==confirmpassword:
        qry1="insert into login VALUES ('','"+dEmail+"','"+str(confirmpassword)+"','Pending')"
        res1=db.insert(qry1)
        qry="insert into doctor values('','"+str(res1)+"','"+dName+"','"+dEmail+"','"+dPhone+"','"+dQualification+"','"+dGender+"','"+dExperiance+"','"+dPlace+"','Pending','"+path+"')"
        res=db.insert(qry)
        return '''<script>alert('Account Created');window.location="/"</script>'''
    else:
        return '''<script>alert('Password Does not Match');window.location="/doctor_signup"</script>'''


@app.route('/view_profile')
def view_profile():
    qry="select * from doctor where Login_id='"+str(session["lid"])+"'"
    db=Db()
    res=db.selectOne(qry)
    return render_template('Doctor/view profile.html',val=res)


@app.route('/dr_viewdisease')
def dr_viewdisease():
    qry="select * from disease"
    db=Db()
    res=db.select(qry)
    return render_template('Doctor/view diseases_dr.html',val=res)


@app.route('/dr_update')
def dr_update():
    qry = "select * from doctor where Login_id='" + str(session["lid"]) + "'"
    db = Db()
    res = db.selectOne(qry)
    return render_template('Doctor/update account.html',val=res)

@app.route('/update_post',methods=['post'])
def update_post():
    uname=request.form['textfield3']
    uemail=request.form['textfield2']
    uphone=request.form['textfield']

    uqualify=request.form['textfield4']
    ugender=request.form['RadioGroup1']
    uexperience=request.form['textfield5']
    uplace=request.form['textfield6']
    db=Db()
    if 'photo' in request.files:
        uimage = request.files['photo']
        date = datetime.now().strftime("%y%m%d-%H%M%S")
        path = "/static/doctor_img/" + str(date) + ".jpg"
        uimage.save("C:\\Users\\91773\\PycharmProjects\\Admin\\static\\doctor_img\\" + str(date) + ".jpg")
        qry="UPDATE doctor SET Name='"+uname+"',Email='"+uemail+"',Phone_No='"+uphone+"',Qualification='"+uqualify+"',Gender='"+ugender+"',Experiance='"+uexperience+"',Place='"+uplace+"',Photo='"+path+"' WHERE Login_id='"+str(session['lid'])+"'"
        res1=db.update(qry)
    else:
        qry = "UPDATE doctor SET Name='" + uname + "',Email='" + uemail + "',Phone_No='" + uphone + "',Qualification='" + uqualify + "',Gender='" + ugender + "',Experiance='" + uexperience + "',Place='" + uplace + "' WHERE Login_id='" + str(
            session['lid']) + "'"
        res1 = db.update(qry)
    return '''<script>alert('Updated');window.location="/view_profile"</script>'''

@app.route('/changepassword')
def changepassword():
    return render_template('Doctor/password.html')

@app.route('/changepassword_post',methods=['post'])
def changepassword_post():
    pcurrent=request.form['textfield']
    pnew=request.form['textfield2']
    pconfirm=request.form['textfield3']
    db=Db()
    qry="select * from login where Password='"+pcurrent+"' and Login_id='"+str(session["lid"])+"'"
    res=db.selectOne(qry)
    if res is not None:
        if pnew==pconfirm:
            qry1="update login set Password='"+pconfirm+"' where Login_id='"+str(session["lid"])+"'"
            res1=db.update(qry1)
            return '''<script>alert('Password Changing Successfully');window.location="/"</script>'''
        else:
            return '''<script>alert('password mismatch');window.location="/changepassword"</script>'''
    else:
        return '''<script>alert('Not Found');window.location="/changepassword"</script>'''

@app.route('/add_schedule')
def add_schedule():
    return render_template('Doctor/addschedule.html')

@app.route('/add_schedule_post',methods=['post'])
def add_schedule_post():
    date=request.form['textfield3']
    from_time=request.form['textfield']
    to_time=request.form['textfield2']
    db=Db()
    qry="insert into schedule (`Doctor_id`,`Date`,`From_time`,`To_time`)values('"+str(session["lid"])+"','"+date+"','"+from_time+"','"+to_time+"')"
    res=db.insert(qry)
    return '''<script>alert('Schedule added');window.location="/schedule_manage"</script>'''

@app.route('/schedule_manage')
def schedule_manage():
    db=Db()
    qry="select * from schedule"
    res=db.select(qry)
    return render_template('Doctor/schedule_management.html',val=res)

@app.route('/edit_schedule/<id>')
def edit_schedule(id):
    db=Db()
    session["SHID"]=id
    qry="select * from schedule where Schedule_id='"+str(id)+"'"
    res=db.selectOne(qry)
    return render_template('Doctor/edit schedule.html',val=res)

@app.route('/edit_schedule_post',methods=['post'])
def edit_schedule_post():
    edate=request.form['textfield3']
    efrom_time=request.form['textfield']
    eto_time=request.form['textfield2']
    db=Db()
    qry="update schedule set Date='"+edate+"',From_time='"+efrom_time+"',To_time='"+eto_time+"' where Schedule_id='"+str(session["SHID"])+"'"
    res=db.update(qry)
    return schedule_manage()

@app.route('/delete_schedule/<id>')
def delete_schedule (id):
    db=Db()
    qry="delete from schedule where Schedule_id='"+str(id)+"'"
    res=db.delete(qry)
    return '''<script>alert('Deleted');window.location="/schedule_manage"</script>'''

@app.route('/appointments/<id>')
def appointments(id):
    db=Db()
    qry="SELECT * FROM `user` INNER JOIN `appointment`   ON `user`.`Login_id` = `appointment`.`User_id` WHERE `appointment`.`Schedule_id`='"+id+"'"
    res=db.select(qry)
    return render_template('Doctor/appointment.html', val=res)

@app.route('/view_appointment')
def view_appointment():
    db=Db()
    qry="select * from appointment inner join user"
    res=db.select(qry)
    return render_template('Doctor/view appointment.html',val=res)

@app.route('/view_review')
def view_review():
    db = Db()
    qry="SELECT * FROM `reviews` INNER JOIN `user` ON `user`.`Login_id`=`reviews`.`User_id` WHERE Doctor_id='"+str(session['lid'])+"'"
    res=db.select(qry)
    return render_template('Doctor/view review.html',val=res)



@app.route('/usersignup',methods=['post'])
def usersignup():
    name=request.form['Name']
    email=request.form['Email']
    phone=request.form['Phone_No']
    age=request.form['Age']
    gender=request.form['Gender']
    place=request.form['Place']
    photo=request.form['Photo']
    password=request.form['password']
    db=Db()
    import time, datetime
    from encodings.base64_codec import base64_decode
    import base64

    timestr = time.strftime("%Y%m%d-%H%M%S")
    print(timestr)
    a = base64.b64decode(photo)
    fh = open("C:\\Users\\91773\\PycharmProjects\\Admin\\static\\user\\" + timestr + ".jpg", "wb")
    path = "/static/user/" + timestr + ".jpg"
    fh.write(a)
    fh.close()

    qry1="insert into login (Username,Password,User_Type) values('"+email+"','"+password+"','user')"
    lid=db.insert(qry1)
    qry="insert into user (Login_id,Name,Email,Phone_No,Age,Gender,Place,Photo) values ('"+str(lid)+"','"+name+"','"+email+"','"+phone+"','"+age+"','"+gender+"','"+place+"','"+path+"')"
    db.insert(qry)

    return jsonify(status='ok')





@app.route('/user_changepassword',methods=['post'])
def user_changepassword():
    lid=request.form['lid']
    newpassword=request.form['newpassword']

    qry="update login set Password='"+newpassword+"' where Login_id='"+lid+"'"
    print(qry)
    db=Db()
    db.update(qry)

    return jsonify(status='ok')



@app.route('/user_viewprofile',methods=['post'])
def user_viewprofile():
    lid=request.form['lid']
    qry="select * from user WHERE Login_id='"+lid+"' "
    db=Db()
    res=db.selectOne(qry)
    return jsonify(status='ok',data=res)



@app.route('/user_editprofile',methods=['post'])
def user_editprofile():
    name=request.form['Name']
    email=request.form['Email']
    phone=request.form['Phone_No']
    age=request.form['Age']
    gender=request.form['Gender']
    place=request.form['Place']
    photo=request.form['Photo']
    lid=request.form['lid']
    db=Db()


    if len(photo)>0:
        import time, datetime
        from encodings.base64_codec import base64_decode
        import base64

        timestr = time.strftime("%Y%m%d-%H%M%S")
        print(timestr)
        a = base64.b64decode(photo)
        fh = open("C:\\Users\\91773\\PycharmProjects\\Admin\\static\\user\\" + timestr + ".jpg", "wb")
        path = "/static/user/" + timestr + ".jpg"
        fh.write(a)
        fh.close()

        qry="UPDATE USER SET NAME='"+name+"',Email='"+email+"',Phone_No='"+phone+"',Age='"+age+"',Gender='"+gender+"',Place='"+place+"',Photo='"+path+"' where Login_id ='"+lid+"'"
        print(qry)
        res=db.update(qry)

    else:

        qry1="UPDATE USER SET NAME='"+name+"',Email='"+email+"',Phone_No='"+phone+"',Age='"+age+"',Gender='"+gender+"',Place='"+place+"' where Login_id ='"+lid+"'"
        print(qry1)
        res1=db.update(qry1)


    return jsonify(status='ok')



@app.route('/user_viewdoctor',methods=['post'])
def user_viewdoctor():
    qry="select * from doctor "
    db=Db()
    res=db.select(qry)
    return jsonify(status='ok',data=res)


@app.route('/user_viewdoctorschedule',methods=['post'])
def user_viewdoctorschedule():
    lid=request.form['lid']
    qry="select * from schedule where Doctor_id='"+lid+"' "
    print(qry)
    db=Db()
    res=db.select(qry)
    return jsonify(status='ok',data=res)

@app.route('/user_addappointment',methods=['post'])
def user_addappointment():
    sid=request.form['Schedule_id']
    uid=request.form['User_id']
    qry="INSERT INTO `appointment`(`Schedule_id`,`User_id`,`Date`)VALUES('"+sid+"','"+uid+"',CURDATE())"
    db=Db()
    res=db.insert(qry)
    return jsonify(status='ok')


@app.route('/user_typefeedback',methods=['post'])
def user_typefeedback():
    uid=request.form['User_id']
    feedback=request.form['Feedback']
    qry="INSERT INTO `feedback`(`User_id`, `Date`, `Time`, `Feedback`)VALUES('"+uid+"', CURDATE(), CURTIME(), '"+feedback+"')"
    db=Db()
    res=db.insert(qry)
    return jsonify(status='ok')



@app.route('/user_viewdisease',methods=['post'])
def user_viewdisease ():
    qry="select * from disease"
    db=Db()
    res=db.select(qry)
    return jsonify(status='ok',data=res)


@app.route('/user_viewsymptom',methods=['post'])
def user_viewsymptom ():
    qry="SELECT DISTINCT `Symptoms` FROM symptoms"
    db=Db()
    res=db.select(qry)
    return jsonify(status='ok',data=res)


@app.route('/user_viewappointment',methods=['post'])
def user_viewappointment():
    uid=request.form['lid']
    qry="SELECT * FROM appointment INNER JOIN `schedule` on  `appointment`.`Schedule_id`=`schedule`.`Schedule_id` INNER JOIN `doctor` ON `doctor`.`Login_id`=`schedule`.`Doctor_id` WHERE `appointment`.`User_id`='"+uid+"'"
    db=Db()
    res=db.select(qry)
    return jsonify(status='ok',data=res)

@app.route('/user_login',methods=['post'])
def user_login ():
    username = request.form['Username']
    password = request.form['Passwword']
    db = Db()
    qry = "select * from login where Username='" + username + "' and Password='" + password + "'"
    print(qry)
    res = db.selectOne(qry)
    if res is not None:
        return jsonify(status='ok',lid=res['Login_id'],type=res['User_Type'])
    else:
        return jsonify(status='no')





@app.route('/upload_file_predict', methods=['POST'])
def upload_file_predict():
    photo = request.form['Photo']
    db = Db()
    import time, datetime
    from encodings.base64_codec import base64_decode
    import base64

    timestr = time.strftime("%Y%m%d-%H%M%S")
    print(timestr)
    a = base64.b64decode(photo)
    fh = open("C:\\Users\\91773\\PycharmProjects\\Admin\\static\\prediction\\" + timestr + ".jpg", "wb")
    path = "/static/prediction/" + timestr + ".jpg"
    fh.write(a)
    fh.close()



    md = "C:\\Users\\91773\\PycharmProjects\\Admin\\static\\prediction\\" + timestr + ".jpg"
    import numpy as np
    from skimage import io, color, img_as_ubyte

    from skimage.feature import greycomatrix, greycoprops
    from sklearn.metrics.cluster import entropy

    rgbImg = io.imread(md)
    grayImg = img_as_ubyte(color.rgb2gray(rgbImg))

    distances = [1, 2, 3]
    angles = [0, np.pi / 4, np.pi / 2, 3 * np.pi / 4]
    properties = ['energy', 'homogeneity']

    glcm = greycomatrix(grayImg,
                            distances=distances,
                            angles=angles,
                            symmetric=True,
                            normed=True)

    feats = np.hstack([greycoprops(glcm, 'homogeneity').ravel() for prop in properties])
    feats1 = np.hstack([greycoprops(glcm, 'energy').ravel() for prop in properties])
    feats2 = np.hstack([greycoprops(glcm, 'dissimilarity').ravel() for prop in properties])
    feats3 = np.hstack([greycoprops(glcm, 'correlation').ravel() for prop in properties])
    feats4 = np.hstack([greycoprops(glcm, 'contrast').ravel() for prop in properties])

    k = np.mean(feats)
    l = np.mean(feats1)
    m = np.mean(feats2)
    n = np.mean(feats3)
    o = np.mean(feats4)

        # test=np.array([[k,l,m,n,o]])

    test = [[k, l, m, n, o]]
    print(test, "aaa")
    print(k)
    print(l)
    print(m)
    print(n)
    print(o)
    db=Db()
    qry="select * from glcm"
    res = db.select(qry)
    attribut = []
    lables = []

    for i in res:
        features = [float(i["homogeneity"]), float(i["energy"]), float(i["dissimilarity"]), float(i["correlation"]), float(i["contrast"])]

        lables.append(i['diseasename'])
        attribut.append(features)

    attribut = np.array(attribut)
    lables = np.array(lables)

    print(attribut, "aaaaaa")

    print("Labels", lables)

    from sklearn.ensemble import RandomForestClassifier
    rnd = RandomForestClassifier()
    rnd.fit(attribut, lables)
    print(attribut, lables, "kkk")
    k = rnd.predict(np.array(test))

    print("randome forest result", k)

        ##############imps

    id = k[0]
    print(id)
    qry2="SELECT * FROM `disease` WHERE `Disease_Name`='"+id+"'"
    res2 = db.selectOne(qry2)
    if res is not None:

        return jsonify(status='ok', name=res2["Disease_Name"], des=res2["Description"],type= res2["Type"],image=res2["Image"])
    else:
        return jsonify(status="no")


















































if __name__ == '__main__':
    app.run(debug=True,host='0.0.0.0')
