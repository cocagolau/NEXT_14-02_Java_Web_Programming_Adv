package me.dec7.di;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MessageRenderer {
	
	private MessageProvider messageProvider;
	
	public void setMessageProvider(MessageProvider messageProvider) {
		this.messageProvider = messageProvider;
	}
	
	public void render() {
		// message provider 구현체는 강한 커플링 관계
		// dependency injection에서는 이 둘의 관계를 느슨하기 위한 목적
		
//		MessageProvider mp = new HelloWorldMessageProvider();
//		MessageProvider mp = new HiWorldMessageProvider();
//		System.out.println(mp.getMessage());
		
		System.out.println(this.messageProvider.getMessage());
	}
	
	
	/*
	 * junit기반으로 테스로 변경
	 * 
	public static void main(String[] args) {
		
		MessageRenderer renderer = new MessageRenderer();
		
		// spring frame work의 설정파일을 이용해서 di를 함.		
		renderer.setMessageProvider(new HelloWorldMessageProvider());
		renderer.render();
		
		renderer.setMessageProvider(new HiWorldMessageProvider());
		renderer.render();
		
		
		
		
		// di.xml을 읽어서 di할 수 있도록 적용
		// 설정 파일을 읽어서 관리하는 Class를 ApplicationContext라고 부름, interface
		// 그 중 xml을 읽어서 context를 만들 수 있는 Class
		ApplicationContext ac = new ClassPathXmlApplicationContext("di.xml");
		
		// getBean의 반환값은 Obejct이므로 Casting이 필요함		
		MessageRenderer renderer = (MessageRenderer) ac.getBean("messageRenderer");
		
		renderer.render();
		
		
		
		
	}
*/

}
