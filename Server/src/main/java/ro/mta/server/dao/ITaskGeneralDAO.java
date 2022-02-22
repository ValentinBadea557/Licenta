package ro.mta.server.dao;

import java.time.LocalDateTime;

public interface ITaskGeneralDAO {
    public String addTaskGeneral(String name, String periodicity, int duration, LocalDateTime starttime, LocalDateTime deadline);
    public int getIDBasedOnName(String name,LocalDateTime start,LocalDateTime deadline);
}
