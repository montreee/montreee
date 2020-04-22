FROM openjdk
COPY . /usr/lib/montreee
WORKDIR /usr/lib/montreee
RUN ["chmod", "+x", "./montreee"]
ENV PATH="/usr/lib/montreee:${PATH}"
ENTRYPOINT ["montreee"]
