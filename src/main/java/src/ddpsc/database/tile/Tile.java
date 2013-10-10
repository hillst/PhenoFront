package src.ddpsc.database.tile;

public class Tile {
	private String cameraLabel;
	private int rawImageOid; //maybe just go fetch the path
	private int rawNullImageOid;
	private int dataFormat;
	private int width;
	private int height;
	private int rotateFlipType;
	private int frame;
	private String imagePath;
	
	public Tile(String cameraLabel, int rawImageOid, int rawNullImageOid,
			int dataFormat, int width, int height, int rotateFlipType, int frame, String imagePath) {
		super();
		this.cameraLabel = cameraLabel;
		this.rawImageOid = rawImageOid;
		this.rawNullImageOid = rawNullImageOid;
		this.dataFormat = dataFormat;
		this.width = width;
		this.height = height;
		this.rotateFlipType = rotateFlipType;
		this.frame = frame;
	}
	public Tile(){}
	
	public String getNullImagePath(){
		return this.imagePath + "/" + new Integer(this.rawNullImageOid).toString();
	}
	
	@Override
	public String toString() {
		return "Tile [cameraLabel=" + cameraLabel + ", rawImageOid="
				+ rawImageOid + ", rawNullImageOid=" + rawNullImageOid
				+ ", dataFormat=" + dataFormat + ", width=" + width
				+ ", height=" + height + ", rotateFlipType=" + rotateFlipType
				+ ", frame=" + frame + ", imagePath=" + imagePath + "]";
	}
	public String getRawImagePath(){
		return this.imagePath + "/" + new Integer(this.rawImageOid).toString();
	}
	
	public String getImagePath(){
		return this.imagePath;
	}
	public void setImagePath(String imagePath){
		this.imagePath = imagePath;
	}
	public String getCameraLabel() {
		return cameraLabel;
	}
	public void setCameraLabel(String cameraLabel) {
		this.cameraLabel = cameraLabel;
	}
	public int getRawImageOid() {
		return rawImageOid;
	}
	public void setRawImageOid(int rawImageOid) {
		this.rawImageOid = rawImageOid;
	}
	public int getRawNullImageOid() {
		return rawNullImageOid;
	}
	public void setRawNullImageOid(int rawNullImageOid) {
		this.rawNullImageOid = rawNullImageOid;
	}
	public int getDataFormat() {
		return dataFormat;
	}
	public void setDataFormat(int dataFormat) {
		this.dataFormat = dataFormat;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getRotateFlipType() {
		return rotateFlipType;
	}
	public void setRotateFlipType(int rotateFlipType) {
		this.rotateFlipType = rotateFlipType;
	}
	public int getFrame() {
		return frame;
	}
	public void setFrame(int frame) {
		this.frame = frame;
	}
	
	
}
