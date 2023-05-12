import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BankersAlgorithm extends JFrame {
    JTextField textTotalRes, textAvailableRes;
    JTextField processID, maxMatrix, allocationMatrix, needMatrix;
    JButton requestButton, calculateButton;
    JLabel labelProcessID, labelAllocatedRes, labelMaxRes, labelNeededRes;
    
    int totalRes[];
    int availableRes[];
    int allocation[][];
    int max[][];
    int need[][];
    int processCount;
    
    BankersAlgorithm(){
        setLayout(new GridLayout(10,2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        labelProcessID = new JLabel("Enter Process ID: ");
        add(labelProcessID);
        processID = new JTextField();
        add(processID);
        
        labelTotalRes = new JLabel("Total Resources: ");
        add(labelTotalRes);
        textTotalRes = new JTextField();
        add(textTotalRes);
        
        labelAvailableRes = new JLabel("Available Resources: ");
        add(labelAvailableRes);
        textAvailableRes = new JTextField();
        add(textAvailableRes);
        
        labelAllocatedRes = new JLabel("Allocated Resources: ");
        add(labelAllocatedRes);
        allocationMatrix = new JTextField();
        add(allocationMatrix);
        
        labelMaxRes = new JLabel("Maximum Resources: ");
        add(labelMaxRes);
        maxMatrix = new JTextField();
        add(maxMatrix);
        
        labelNeededRes = new JLabel("Needed Resources: ");
        add(labelNeededRes);
        needMatrix = new JTextField();
        add(needMatrix);
        
        requestButton = new JButton("Request Resources");
        add(requestButton);
        requestButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                resourceRequest();
            }
        });
        
        calculateButton = new JButton("Calculate Safe Sequence");
        add(calculateButton);
        calculateButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                safeSequence();
            }
        });
        
        setSize(500,500);
        setVisible(true);
    }
    
    public void resourceRequest(){
        
        int processID = Integer.parseInt(this.processID.getText());
        int requestedRes[] = new int[totalRes.length];
        for(int i=0; i<totalRes.length; i++){
            requestedRes[i] = Integer.parseInt(JOptionPane.showInputDialog("Requested " + i + " resource"));
        }
        
        
        if( bankerAlgo(processID, requestedRes) ){
            JOptionPane.showMessageDialog(null, "Request granted!");
        }else{
            JOptionPane.showMessageDialog(null, "Request denied!"); 
        }
    }
    
    public boolean bankerAlgo(int processID, int requestedRes[]){
         
        int availableRes[] = new int[totalRes.length];
        for(int i=0; i<availableRes.length; i++){
            availableRes[i] = this.availableRes[i] + allocation[processID][i] - requestedRes[i];
        }
        int needRes[] = new int[totalRes.length];
        for(int i=0; i<needRes.length; i++){
            needRes[i] = max[processID][i] - requestedRes[i];
        }
        
        
        for(int i=0; i<totalRes.length; i++){
            if(requestedRes[i] > needRes[i] || requestedRes[i] > availableRes[i])
                return false;
        }
        
         
        for(int i=0; i<processCount; i++){
            if(i != processID){
                if(!bankerAlgo(i, requestedRes))
                    return false;
            }
        }
        return true;
    }
    
    public void safeSequence(){
        
        int workAvailable[] = new int[totalRes.length];
        int workNeed[][] = new int[processCount][totalRes.length];
        for(int i=0; i<totalRes.length; i++){
            workAvailable[i] = availableRes[i];
            for(int j=0; j<processCount; j++){
                workNeed[j][i] = need[j][i];
            }
        } 
        
        
        int safeSeq[] = new int[processCount];
        int count = 0;
        while(count < processCount){
            boolean found = false;
            for(int i=0; i<processCount; i++){
                int j;
                for(j=0; j<totalRes.length; j++){
                    if(workNeed[i][j] > workAvailable[j])
                        break;
                }
                if(j == totalRes.length){   
                    found = true;
                    safeSeq[count++] = i;
                    for(int k=0; k<totalRes.length; k++)
                        workAvailable[k] += allocation[i][k];
                    workNeed[i][j] = 0;
                }  
            }
            if(!found)   
                JOptionPane.showMessageDialog(null, "System is in Deadlock!");
        }
        
        
        String safeSeqStr = "Safe Sequence: < ";
        for(int i=0; i<safeSeq.length; i++)
            safeSeqStr += safeSeq[i] + " ";
        safeSeqStr += ">";
        JOptionPane.showMessageDialog(null, safeSeqStr);
    }
}
