package com.net2plan.gui.plugins.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * <h2>This class evaluates the membership of specific IP Address to a Network CIDR</h2>
 *
 * @author Cesar San-Nicolas-Martinez
 */
public class IPAddressComparator
{
	private final int			mask;
	private final InetAddress	requiredAddress;

	/**
	 * Takes a specific IP address or a range specified using the IP/Netmask.
	 *
	 * @param ipAddress network address (e.g. 192.168.1.0/24)
	 */
	public IPAddressComparator(String ipAddress)
	{

		if (ipAddress.indexOf('/') > 0)
		{
			final String[] addressAndMask = ipAddress.split("/");
			ipAddress = addressAndMask[0];
			mask = Integer.parseInt(addressAndMask[1]);
		} else
		{
			mask = 0;
		}
		requiredAddress = parseAddress(ipAddress);
	}

	/**
	 * Checks if the given address is in the same network as the main IPAddress
	 * @param address address to check
	 * @return true if they belong to the same network, false otherwise;
	 */
	public boolean belongsToThisNetwork(String address)
	{
		final InetAddress remoteAddress = parseAddress(address);

		if (mask == 0)
			return remoteAddress.equals(requiredAddress);

		final byte[] remAddr = remoteAddress.getAddress();
		final byte[] reqAddr = requiredAddress.getAddress();

		final int oddBits = mask % 8;
		final int nMask = mask / 8 + (oddBits == 0 ? 0 : 1);
		final byte[] mask = new byte[nMask];

		Arrays.fill(mask, 0, oddBits == 0 ? mask.length : mask.length - 1, (byte) 0xFF);

		if (oddBits != 0)
		{
			int finalByte = (1 << oddBits) - 1;
			finalByte <<= 8 - oddBits;
			mask[mask.length - 1] = (byte) finalByte;
		}

		for (int i = 0; i < mask.length; i++)
			if ((remAddr[i] & mask[i]) != (reqAddr[i] & mask[i]))
				return false;

		return true;
	}

	/**
	 * Translates a host name (machine name or IP) String into a InetAddress
	 * @param address address to parse
	 * @return InetAddress of the host name
	 */
	private InetAddress parseAddress(String address)
	{
		try
		{
			return InetAddress.getByName(address);
			
		} catch (UnknownHostException e)
		{
			throw new IllegalArgumentException("Failed to parse address" + address, e);
		}
	}
}
