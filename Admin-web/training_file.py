import numpy
from skimage.feature import greycomatrix, greycoprops
from skimage import io, color, img_as_ubyte
from sklearn.metrics.cluster import entropy
from DBConnection import Db


# import OS module
import os

# Get the list of all files and directories
path = "C:\\Users\\91773\\PycharmProjects\\Admin\\static\\dataset\\"
dir_list = os.listdir(path)


# prints all files
print(dir_list)
for i in dir_list:
    print(i,"----------")
    ff=path+"\\"+i
    filee=os.listdir(ff)
    for j in filee:
        try:
            print(j)
            rgbImg = io.imread("C:\\Users\\91773\\PycharmProjects\\Admin\\static\\dataset\\"+i+"\\"+j)
            grayImg = img_as_ubyte(color.rgb2gray(rgbImg))

            distances = [1, 2, 3]
            angles = [0, numpy.pi / 4, numpy.pi / 2, 3 * numpy.pi / 4]
            properties = ['energy', 'homogeneity']

            glcm = greycomatrix(grayImg,
                                distances=distances,
                                angles=angles,
                                symmetric=True,
                                normed=True)

            feats = numpy.hstack([greycoprops(glcm, 'homogeneity').ravel() for prop in properties])
            feats1 = numpy.hstack([greycoprops(glcm, 'energy').ravel() for prop in properties])
            feats2 = numpy.hstack([greycoprops(glcm, 'dissimilarity').ravel() for prop in properties])
            feats3 = numpy.hstack([greycoprops(glcm, 'correlation').ravel() for prop in properties])
            feats4 = numpy.hstack([greycoprops(glcm, 'contrast').ravel() for prop in properties])

            homogeneity= numpy.mean(feats)
            print(homogeneity)
            energy = numpy.mean(feats1)
            print(energy)
            dissimilarity = numpy.mean(feats2)
            print(dissimilarity)
            correlation = numpy.mean(feats3)
            print(correlation)
            contrast = numpy.mean(feats4)
            print(contrast)

            qry="INSERT INTO glcm VALUES('','"+str(i)+"','"+str(homogeneity)+"','"+str(energy)+"','"+str(dissimilarity)+"','"+str(correlation)+"','"+str(contrast)+"')"
            db=Db()
            res=db.insert(qry)
        except:
            print("err")
