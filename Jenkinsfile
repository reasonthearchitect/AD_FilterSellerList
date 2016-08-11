node {
    stage 'Checkout'
    git url: 'https://github.com/reasonthearchitect/AD_FilterSellerList.git'

    stage 'Build'
    sh "./gradlew clean build"

    stage 'BuildRunDocker'
    //sh 'docker kill filtersellerlist'
    //sh 'docker rm filtersellerlist'
    sh 'docker build -t reasonthearchitect/filtersellerlist/filtersellerlist .'
    sh 'docker run -d --name filtersellerlist -p 8940:8940 reasonthearchitect/filtersellerlist/filtersellerlist'
}