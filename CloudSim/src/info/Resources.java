package info;

import info.image.Image;
import info.image.WebImageRepository;

public class Resources {

	private int bandWidht;
	private int vCpus;
	private double disk;
	private int ram;

	public Resources(int vCpus, int ram, double disk, int bandWidht) {
		this.vCpus = vCpus;
		this.disk = disk;
		this.ram = ram;
		this.bandWidht = bandWidht;
	}

	public boolean canAllocateVcpu(int vcpu) {
		return this.vCpus - vcpu >= 0;
	}

	private void allocateVcpu(int vcpu) {
		if (canAllocateVcpu(vcpu)) {
			this.vCpus -= vcpu;
		}
	}

	private void deallocateVcpu(int vcpu) {
		this.vCpus += vcpu;
	}

	public boolean canAllocateRam(int ram) {
		return this.ram - ram >= 0;
	}

	private void allocateRam(int ram) {
		if (canAllocateRam(ram)) {
			this.ram -= ram;
		}
	}

	private void deallocateRam(int ram) {
		this.ram += ram;
	}

	public boolean canAllocateDisk(double diskSize) {
		return this.disk - diskSize >= 0;
	}

	private void allocateDisk(double diskSize) {
		if (canAllocateDisk(diskSize)) {
			this.disk -= diskSize;
		}
	}

	private void deallocateDisk(double diskSize) {
		this.disk += diskSize;
	}

	public boolean canAnswerRequest(Request r) {
		return canAllocateVcpu(r.getVCPUs()) && canAllocateRam(r.getRam()) && canAllocateDisk(r.getDiskSize());
	}
	
	/* In seconds */
	public int timeToDownload(Image i) {
		return i.getSize() / this.bandWidht;
	}
	
	/* In seconds */
	public int timeToDownload(int imageID) {
		return WebImageRepository.getInstance().getImage(imageID).getSize() / this.bandWidht;
	}

	public void allocate(int vcpu, int ram, double disk) {
		this.allocateVcpu(vcpu);
		this.allocateRam(ram);
		this.allocateDisk(disk);
	}
	
	public void deallocate(int vcpu, int ram, double disk) {
		this.deallocateVcpu(vcpu);
		this.deallocateRam(ram);
		this.deallocateDisk(disk);
	}

}
